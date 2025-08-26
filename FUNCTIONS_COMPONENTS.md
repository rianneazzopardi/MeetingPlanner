### Project API Overview

- **Scope**: Core classes, responsibilities, and public APIs (methods/fields intended for use outside the class).
- **Note**: Methods without an explicit access modifier are package-private in Java and are excluded unless otherwise noted.

### Main
- **Role**: Application entry point; bootstraps sets, loads/saves state, and drives CLI menus.
- **Public API**:
  - `static void main(String[] args)`
  - `static java.util.Scanner getScanner()`

### Utils
- **Role**: Console input helpers.
- **Public API**:
  - `static int readInteger()`

### Resource (abstract)
- **Role**: Base type for all resources (e.g., `Person`, `Room`).
- **Public API**:
  - `abstract String toDisplayString()`

### Person
- **Role**: Represents a person resource.
- **Public API**:
  - `String getName()` / `void setName(String name)`
  - `String getSurname()` / `void setSurname(String surname)`
  - `String getId()` / `void setId(String id)`
  - `String getPosition()` / `void setPosition(String position)`
  - `String toString()`
  - `String toDisplayString()`
  - `boolean equals(Object o)` / `int hashCode()`
  - `int compareTo(Person other)`

### Room
- **Role**: Represents a room resource.
- **Public API**:
  - `int getRoomNumber()` / `void setRoomNumber(int roomNumber)`
  - `int getRoomCapacity()` / `void setRoomCapacity(int roomCapacity)`
  - `String toString()`
  - `String toDisplayString()`
  - `boolean equals(Object o)` / `int hashCode()`
  - `int compareTo(Room other)`

### Meeting (abstract)
- **Role**: Base meeting with timing and resource set.
- **Public API**:
  - `java.util.Calendar getStartTime()` / `void setStartTime(Calendar startTime)`
  - `java.util.Calendar getEndTime()` / `void setEndTime(Calendar endTime)`
  - `java.util.Set<Resource> getResources()` / `void setResources(Set<Resource> resources)`
  - `int getId()`
  - `String toString()`
  - `String toDisplayString()`
- **Subclass hook**:
  - `protected abstract void checkResources(Set<Resource> resources)`

### WebinarMeeting
- **Role**: Meeting with ≥1 person and 0 rooms.
- **Public API**: Inherits `Meeting` API.

### GroupMeetMeeting
- **Role**: Meeting with exactly 1 room and 2..capacity people.
- **Public API**: Inherits `Meeting` API.

### MeetupMeeting
- **Role**: Meeting with exactly 1 room and exactly 2 people.
- **Public API**: Inherits `Meeting` API.

### ResourceSet<E extends Resource> (abstract)
- **Role**: Generic resource collection with CLI management.
- **Public API**:
  - `void showMenu()`
  - `void displayAll()`
  - `abstract String getMenuTitle()`
  - `abstract void addResource()`
  - `abstract void removeResource()`
  - `abstract void editResource()`

### PersonSet
- **Role**: Manages `Person` resources.
- **Public API**:
  - Inherits `ResourceSet` public methods
  - `Person getPersonByID(String id)`

### RoomSet
- **Role**: Manages `Room` resources.
- **Public API**:
  - Inherits `ResourceSet` public methods
  - `Room getRoomByNumber(int roomNumber)`

### MeetingSet
- **Role**: Manages `Meeting` instances (create/edit/remove/display), ensures no clashes.
- **Public API**:
  - `void showMenu()`
  - `void displayAll()`
  - `java.util.Set<Meeting> getSet()`