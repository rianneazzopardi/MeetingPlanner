# Meeting Organizer - API and Usage Documentation

## Overview
A simple console-based meeting organizer. It manages `Person`s, `Room`s, and `Meeting`s (with types: `WebinarMeeting`, `GroupMeetMeeting`, `MeetupMeeting`). Users interact through menus in `Main`, which coordinate `PersonSet`, `RoomSet`, and `MeetingSet`.

- Core domain:
  - `Resource` (base type)
  - `Person`, `Room`
  - `Meeting` (abstract) with `WebinarMeeting`, `GroupMeetMeeting`, `MeetupMeeting`
- Collections and menus:
  - `ResourceSet<E extends Resource>` (abstract menu-enabled collection)
  - `PersonSet`, `RoomSet`
  - `MeetingSet`
- Utilities: `Utils.readInteger()`
- Entry point: `Main`

---

## Entry Point: `Main`
- Fields: `personSet`, `roomSet`, `meetingSet` – live collections and menus.
- `main(String[] args)`: initializes sets, loads saved state from `save.sav`, then shows the main menu.
- `showMenu()`: main navigation. Options: Manage People, Rooms, Meetings, Query, Save, Exit. On Save/Exit it persists all sets.
- `queryMenu()`: list all meetings that include a given `Room` or `Person`.
- `save()`: serializes `PersonSet`, `RoomSet`, `MeetingSet` to `save.sav`.
- `load()`: if `save.sav` exists, deserializes into the sets.
- `getScanner()`: shared `Scanner` used across menus.

### Example: Run and navigate
```bash
# From project root
javac src/*.java && java -cp src Main
```
- Add people and rooms via their menus.
- Add meetings in "Manage Meetings"; the system validates inputs and clashes.
- Use "Query" to list meetings for a given person or room.

---

## Utilities
### `Utils`
- `static int readInteger()`: Reads an integer from `Main.getScanner()`; repeats until valid. Handles `InputMismatchException` by discarding the bad token and prompting again.

Usage:
```java
int value = Utils.readInteger();
```

---

## Base Types
### `Resource`
- Abstract, `Serializable`.
- Contract: subclasses must implement `String toDisplayString()` for list displays.

### `Person`
- Extends `Resource`, implements `Comparable<Person>` by `id`.
- Fields: `name`, `surname`, `id`, `position`.
- Methods:
  - Getters/setters for all fields
  - `toString()`: "name surname (id: id) - position"
  - `toDisplayString()`: tab-separated summary
  - `equals/hashCode()`: by `id`
  - `compareTo(Person)`: by `id`

Example:
```java
Person p = new Person("Ada", "Lovelace", "A001", "Engineer");
System.out.println(p.toDisplayString());
```

### `Room`
- Extends `Resource`, implements `Comparable<Room>` by `roomNumber`.
- Fields: `roomNumber`, `roomCapacity`.
- Methods:
  - Getters/setters
  - `toString()`: "<number> (capacity: <capacity>)"
  - `toDisplayString()`: labeled, tab-separated fields
  - `equals/hashCode()`: by `roomNumber`
  - `compareTo(Room)`: by `roomNumber`

Example:
```java
Room r = new Room(101, 6);
System.out.println(r);
```

---

## Collections and Menus
### `ResourceSet<E extends Resource>`
- Abstract `Serializable` collection with built-in console menu.
- Protected: `Set<E> set` (backing store).
- Abstracts:
  - `String getMenuTitle()`
  - `void addResource()`
  - `void removeResource()`
  - `void editResource()`
- Concrete:
  - `void showMenu()`: loop showing Add/Remove/Edit/Display/Back and dispatching.
  - `void displayAll()`: iterates `set` and prints `toDisplayString()` for each.

Extend to define resource-specific UX.

### `PersonSet` (extends `ResourceSet<Person>`)
- `getMenuTitle()`: "Manage People".
- `addResource()`: prompts for name, surname, id, position; inserts unless id duplicates an existing `Person`.
- `removeResource()`: prompts for id; removes if found.
- `editResource()`: prompts for id; allows editing name, surname, id (with uniqueness check), position.
- `getPersonByID(String)`: returns `Person` or `null`.

Example flow:
```text
Manage People
1. Add -> enter details
4. Display all -> shows people
```

### `RoomSet` (extends `ResourceSet<Room>`)
- `getMenuTitle()`: "Manage Rooms".
- `addResource()`: prompts for room number and positive capacity; prevents duplicate numbers.
- `removeResource()`: prompts for room number; removes if found.
- `editResource()`: change room number (uniqueness check) or capacity (>0).
- `getRoomByNumber(int)`: returns `Room` or `null`.

---

## Meetings
### `Meeting` (abstract)
- Fields: `int id`, `Set<Resource> resources`, `Calendar startTime`, `Calendar endTime`.
- Constructor enforces `checkResources(resources)` via subclass rules.
- Accessors/mutators for `startTime`, `endTime`, `resources` (setter re-validates via `checkResources`).
- Identity: `equals/hashCode` by `id`.
- Formatting:
  - `toString()`: compact single-line with formatted times
  - `toDisplayString()`: multi-field with embedded `resources` display
- Subclass API: `protected abstract void checkResources(Set<Resource> resources)`.

### `WebinarMeeting`
- Constraints (`checkResources`):
  - At least 1 `Person`.
  - Exactly 0 `Room`s.
- Errors: throws `IllegalArgumentException` with descriptive messages on violation.

### `GroupMeetMeeting`
- Constraints:
  - Exactly 1 `Room`.
  - At least 2 `Person`s.
  - People count must be ≤ room capacity.

### `MeetupMeeting`
- Constraints:
  - Exactly 2 `Person`s.
  - Exactly 1 `Room`.

### `MeetingSet`
- Stores `Set<Meeting>` and manages meeting IDs.
- Menus:
  - `showMenu()`: Add, Remove, Edit, Display All, Exit.
  - `displayAll()`: sorts by `startTime` and prints `toDisplayString()`.
- Creation flow (`addMeeting()`):
  1) Select meeting type (webinar, group meet, meetup)
  2) Input start and end date/time (end must be after start)
  3) Choose resources (people/rooms) with sub-menu
  4) Construct subclass; validation errors are shown and input is retried
  5) On success, meeting is added; if resources clash with existing meetings, user is prompted to edit
- Editing flow (`editMeeting()`): change start, end, or resources (with validation and clash check).
- Resource editing (`showEditMeetingResourcesMenu()`): add/remove/list person or room for a meeting in progress.
- Clash detection (`willClash(Meeting)`): Two meetings clash if times overlap and at least one resource is shared; clashes are printed with the resource and conflicting meeting.
- Helpers:
  - `inputDateAndTime()`: robust prompts for year, month (numeric, full, or abbreviated), day (validated per month), hour (0-23), minute (0-59).
  - `monthToInt(String)`: parse month as number, full, or abbreviated (case-insensitive); returns -1 on failure.

---

## Usage Examples

### Create basic data programmatically
```java
// Build people
Person alice = new Person("Alice", "Ng", "P100", "Manager");
Person bob   = new Person("Bob", "Lee", "P200", "Engineer");

// Build room
Room room101 = new Room(101, 4);

// Add to sets
Main.personSet = new PersonSet();
Main.roomSet = new RoomSet();
Main.meetingSet = new MeetingSet();

Main.personSet.set.add(alice);   // for demonstration only (normally via menus)
Main.personSet.set.add(bob);
Main.roomSet.set.add(room101);
```

### Create a meetup meeting programmatically
```java
java.util.Set<Resource> res = new java.util.HashSet<>();
res.add(alice);
res.add(bob);
res.add(room101);

java.util.Calendar start = new java.util.GregorianCalendar(2025, 0, 10, 9, 0);
java.util.Calendar end   = new java.util.GregorianCalendar(2025, 0, 10, 10, 0);

Meeting meetup = new MeetupMeeting(1, res, start, end);
System.out.println(meetup.toDisplayString());
```

### Use the console UI
```text
Meeting organizer
1. Manage people
2. Manage Rooms
3. Manage Meetings
4. Query
5. Save
6. Exit
```
- Add `Person`s and `Room`s first.
- Add a `Meeting` (type constraints enforced with helpful errors).
- Query to list meetings by selected resource.
- Save to persist; Exit will save then exit.

---

## Notes and Best Practices
- Meeting IDs are auto-assigned within `MeetingSet`.
- Resource uniqueness:
  - `Person`: unique `id`
  - `Room`: unique `roomNumber`
- Validation is eager with informative messages. Handle `IllegalArgumentException` where needed.
- When editing resources, clashes with existing meetings are detected and printed; resolve by adjusting times or resources.