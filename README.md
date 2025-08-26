# Meeting Scheduler Java Application

A Java-based application for scheduling and managing different types of meetings, with resource validation and extensible meeting types.

## Features

- **Multiple Meeting Types**: Supports Meetup, Group Meet, and Webinar meetings, each with specific resource requirements.
- **Resource Validation**: Ensures that each meeting has the correct number and type of resources (people, rooms) according to its type.
- **Extensible Design**: Easily add new meeting types by extending the base `Meeting` class.
- **Error Handling**: Provides clear error messages when invalid resources are assigned to a meeting.

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- (Optional) A Java IDE such as IntelliJ IDEA or Eclipse

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/yourproject.git
   cd yourproject
   ```

2. Compile the source code:
   ```bash
   javac -d out src/*.java
   ```

## Usage

1. Run the application (replace `Main` with your actual main class if different):
   ```bash
   java -cp out Main
   ```

2. The application will prompt you to create meetings and assign resources.

## Project Structure
