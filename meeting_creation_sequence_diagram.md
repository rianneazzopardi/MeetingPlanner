# Meeting Creation Sequence Diagram

This diagram shows the complete workflow for creating a meeting in the Java-based meeting management system.

## Overview

The system supports three types of meetings:
- **Webinar**: Requires 1+ rooms and 1+ people  
- **Group Meet**: Requires 1+ rooms and 3+ people
- **Meetup**: Requires exactly 1 room and exactly 2 people

## Sequence Diagram

```mermaid
sequenceDiagram
    participant User
    participant MeetingSet as MS
    participant Utils as U  
    participant Meeting as M
    participant Resource as R

    User->>MS: addMeeting()
    
    Note over MS: 1. Select Meeting Type
    MS->>User: Display type menu (webinar/group meet/meetup)
    User->>U: Select type (1-3)
    U->>MS: Return type
    
    alt Invalid type
        MS->>User: "invalid option, try again"
        MS->>User: Re-display menu
    end
    
    Note over MS: 2. Input Start Time
    MS->>User: "start date and time:"
    User->>MS: inputDateAndTime()
    MS->>U: Read date/time
    U->>MS: Return start Calendar
    
    Note over MS: 3. Input End Time (with validation)
    loop End time validation
        MS->>User: "end date and time:"
        User->>MS: inputDateAndTime()
        MS->>U: Read date/time
        U->>MS: Return end Calendar
        
        alt End <= Start
            MS->>User: "end time need to be after start time"
        end
    end
    
    Note over MS: 4. Resource Selection
    loop Resource selection and validation
        MS->>User: "choose resources:"
        MS->>MS: showEditMeetingResourcesMenu()
        
        loop Resource menu
            MS->>User: Display menu (add person/room, display, done)
            User->>U: Select option
            U->>MS: Return choice
            
            alt Add Person
                MS->>User: Request person ID
                User->>U: Enter person ID
                U->>MS: Return person ID
                MS->>R: Find person by ID
                R->>MS: Return Person
                MS->>MS: Add to resources
                
            else Add Room
                MS->>User: Request room number
                User->>U: Enter room number
                U->>MS: Return room number
                MS->>R: Find room by number
                R->>MS: Return Room
                MS->>MS: Add to resources
                
            else Display
                MS->>User: Show current resources
            else Done
                break
            end
        end
        
        Note over MS: 5. Create Meeting Object
        alt Webinar
            MS->>M: new WebinarMeeting(id, resources, start, end)
        else Group Meet
            MS->>M: new GroupMeetMeeting(id, resources, start, end)
        else Meetup
            MS->>M: new MeetupMeeting(id, resources, start, end)
        end
        
        M->>M: checkResources(resources)
        
        alt Resource validation fails
            M->>MS: throw IllegalArgumentException
            MS->>User: Display error + "try again"
        else Success
            M->>MS: Return meeting object
            break
        end
    end
    
    Note over MS: 6. Add to System
    MS->>MS: set.add(meeting)
    
    Note over MS: 7. Check Conflicts
    MS->>MS: willClash(meeting)
    
    loop Check existing meetings
        MS->>MS: Check time overlap
        alt Time overlaps + Resource conflict
            MS->>User: Display conflict info
        end
    end
    
    alt Has conflicts
        MS->>User: "Clashing resources, make changes"
        MS->>MS: editMeeting(meeting)
    else No conflicts
        MS->>User: Meeting created successfully
    end
```

## Key Validation Rules

### Meeting Type Requirements:
- **Webinar**: 1+ rooms, 1+ people
- **Group Meet**: 1+ rooms, 3+ people  
- **Meetup**: exactly 1 room, exactly 2 people

### Time Validation:
- End time must be after start time
- System checks for resource conflicts with existing meetings

### Resource Conflict Detection:
- If time periods overlap between meetings
- AND they share the same resources (people or rooms)
- User must edit the meeting to resolve conflicts

## Error Handling

The system provides comprehensive error handling:
1. **Invalid meeting type**: Re-prompts for valid selection
2. **Invalid time range**: Re-prompts for end time
3. **Invalid resource requirements**: Shows specific error message and allows retry
4. **Resource conflicts**: Shows conflicting resources and allows editing

## Files Involved

- `MeetingSet.java`: Main controller for meeting operations
- `Meeting.java`: Abstract base class for meetings
- `WebinarMeeting.java`, `GroupMeetMeeting.java`, `MeetupMeeting.java`: Specific meeting types
- `Resource.java`: Base class for people and rooms
- `Utils.java`: Input handling utilities