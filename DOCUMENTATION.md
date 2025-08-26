## Project Documentation

### Overview
A console-based meeting organizer written in Java. It manages `Person` and `Room` resources, and schedules `Meeting`s of various types with validation and clash detection. Data is persisted via Java serialization to `save.sav`.

---

## Core Abstractions

### `Resource`
Abstract base for all resources that can be attached to meetings.

- Purpose: Provide a serializable base and a human-readable display string.
- Key API:
```java
public abstract class Resource implements Serializable {
    public abstract String toDisplayString();
}
```

### `ResourceSet<E extends Resource>`
Abstract collection manager for a specific `Resource` type. Provides common menus and actions.

- Responsibilities:
  - Maintain a `Set<E>` of resources
  - Provide an interactive menu to add/remove/edit/display resources
- Key API:
```java
public abstract class ResourceSet<E extends Resource> implements Serializable {
    protected Set<E> set;

    public abstract String getMenuTitle();
    public void showMenu();
    public abstract void addResource();
    public abstract void removeResource();
    public abstract void editResource();
    public void displayAll();
}
```

---

## Resources

### `Person`
Represents a person who can participate in meetings.

- Fields: `name`, `surname`, `id`, `position`
- Ordering: Comparable by `id`
- Equality: By `id`
- Display: Full name with id and position
- Key API:
```java
public class Person extends Resource implements Comparable<Person> {
    public Person();
    public Person(String name, String surname, String id, String position);

    public String getName();
    public void setName(String name);
    public String getSurname();
    public void setSurname(String surname);
    public String getId();
    public void setId(String id);
    public String getPosition();
    public void setPosition(String position);

    public String toDisplayString();
    public String toString();
    public boolean equals(Object o);
    public int hashCode();
    public int compareTo(Person other);
}
```

### `PersonSet`
Interactive manager for `Person` resources.

- Menu Title: "Manage People"
- Behaviors:
  - Add: prompts for name, surname, id, position; enforces unique `id`
  - Remove: by `id`
  - Edit: name, surname, id (uniqueness enforced), position
  - Find: `getPersonByID(String)`
- Key API:
```java
public class PersonSet extends ResourceSet<Person> {
    public String getMenuTitle();
    public void addResource();
    public void removeResource();
    public void editResource();
    public Person getPersonByID(String id);
}
```

### `Room`
Represents a physical room.

- Fields: `roomNumber`, `roomCapacity`
- Ordering: Comparable by `roomNumber`
- Equality: By `roomNumber`
- Display: Number and capacity
- Key API:
```java
public class Room extends Resource implements Comparable<Room> {
    public Room();
    public Room(int roomNumber, int roomCapacity);

    public int getRoomNumber();
    public void setRoomNumber(int roomNumber);
    public int getRoomCapacity();
    public void setRoomCapacity(int roomCapacity);

    public String toDisplayString();
    public String toString();
    public boolean equals(Object o);
    public int hashCode();
    public int compareTo(Room other);
}
```

### `RoomSet`
Interactive manager for `Room` resources.

- Menu Title: "Manage Rooms"
- Behaviors:
  - Add: prompts for number and capacity; capacity must be > 0; enforces unique `roomNumber`
  - Remove: by `roomNumber`
  - Edit: room number (uniqueness enforced) or capacity (> 0)
  - Find: `getRoomByNumber(int)`
- Key API:
```java
public class RoomSet extends ResourceSet<Room> {
    public String getMenuTitle();
    public void addResource();
    public void removeResource();
    public void editResource();
    public Room getRoomByNumber(int number);
}
```

---

## Meetings

### `Meeting`
Base class for all meeting types with time window and assigned `Resource`s.

- Fields: `id`, `startTime`, `endTime`, `resources`
- Invariants:
  - Subclasses validate resource composition via `checkResources(Set<Resource>)`
- Display: Compact `toString()` and detailed `toDisplayString()` with resources
- Key API:
```java
public abstract class Meeting implements Serializable {
    public Meeting(int id, Set<Resource> resources, Calendar startTime, Calendar endTime);

    protected abstract void checkResources(Set<Resource> resources);

    public Calendar getStartTime();
    public void setStartTime(Calendar startTime);
    public Calendar getEndTime();
    public void setEndTime(Calendar endTime);

    public Set<Resource> getResources(); // unmodifiable
    public void setResources(Set<Resource> resources); // revalidates and sets

    public int getId();

    public boolean equals(Object o); // by id
    public int hashCode();

    public String toString();
    public String toDisplayString();
}
```

### `WebinarMeeting`
- Rules:
  - At least 1 `Person`
  - Exactly 0 `Room`
- Validation: throws `IllegalArgumentException` on violation
- API:
```java
public class WebinarMeeting extends Meeting {
    public WebinarMeeting(int id, Set<Resource> resources, Calendar startTime, Calendar endTime);
    protected void checkResources(Set<Resource> resources);
}
```

### `GroupMeetMeeting`
- Rules:
  - Exactly 1 `Room`
  - At least 2 `Person`
  - People count must be ≤ `room.roomCapacity`
- Validation: throws `IllegalArgumentException` on violation
- API:
```java
public class GroupMeetMeeting extends Meeting {
    public GroupMeetMeeting(int id, Set<Resource> resources, Calendar startTime, Calendar endTime);
    protected void checkResources(Set<Resource> resources);
}
```

### `MeetupMeeting`
- Rules:
  - Exactly 2 `Person`
  - Exactly 1 `Room`
- Validation: throws `IllegalArgumentException` on violation
- API:
```java
public class MeetupMeeting extends Meeting {
    public MeetupMeeting(int id, Set<Resource> resources, Calendar startTime, Calendar endTime);
    protected void checkResources(Set<Resource> resources);
}
```

### `MeetingSet`
Manages creation, editing, and display of `Meeting`s with clash detection.

- Responsibilities:
  - Assign incremental unique IDs
  - Input/validate date and time components
  - Construct specific meeting type after resource selection
  - Edit start/end times and resources with revalidation
  - Detect clashes: overlapping time ranges sharing any resource
- Important behaviors:
  - `addMeeting()`: interactive creation with validation loops
  - `removeMeeting()`: by `id`
  - `editMeeting()`: pick meeting by `id` then edit fields
  - `displayAll()`: ordered by `startTime`
  - `getSet()`: unmodifiable view of all meetings
- Key API:
```java
public class MeetingSet implements Serializable {
    public void showMenu();
    public void displayAll();
    public Set<Meeting> getSet(); // unmodifiable

    // private helpers (interactive):
    // addMeeting(), removeMeeting(), editMeeting(), editMeeting(Meeting),
    // inputDateAndTime(), monthToInt(String), editResources(Meeting),
    // showEditMeetingResourcesMenu(Set<Resource>), willClash(Meeting)
}
```

---

## Utilities and Entry Point

### `Utils`
Small helper for robust integer input from `Main.getScanner()`.

- Behavior: Loops until a valid `int` is provided; discards invalid tokens
- API:
```java
public final class Utils {
    public static int readInteger();
}
```

### `Main`
Program entry point; wires sets, loads/saves state, and exposes main menus.

- Files: Persists to `save.sav` in working directory
- Menus: People, Rooms, Meetings, Query, Save, Exit
- Query: Filter meetings containing a resource (person by id or room by number)
- API:
```java
public final class Main {
    public static void main(String[] args);
    public static Scanner getScanner();
}
```

---

## Usage Notes
- Resource uniqueness:
  - `Person`: unique `id`
  - `Room`: unique `roomNumber`
- Meeting resource rules enforced per subtype; violations throw `IllegalArgumentException`.
- Clash detection: Two meetings clash if time windows overlap and share at least one resource; editing will be requested until resolved.
- Persistence: Data is serialized/deserialized automatically on Save/Exit and startup if `save.sav` exists.

---

## Extensibility
- Add a new resource type: Create a class extending `Resource` and a corresponding `ResourceSet` implementation.
- Add a new meeting type: Extend `Meeting` and implement `checkResources(Set<Resource>)`; integrate selection in `MeetingSet.addMeeting()`.