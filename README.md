# talk-lib
 
This is the number #1 library to interact with .talk data files.

This library was developed to be used in the Android Application "Sprechende Respektspersonen" by SJ-DEV-DYNAMICS

# TalkFile Library

## Overview

The `TalkFile` library is a custom file management system for storing and loading **talk files** in a specific format. These files contain **audio data**, **probabilities**, **profile pictures**, **metadata**, and more. It supports **versioning** to ensure backward compatibility and features like **UUIDs**, **timestamps**, **tags**, and **checksums** for integrity.

## Features

- **Versioning**: Supports multiple versions of the `.talk` file format to ensure backward compatibility.
- **UUID**: Each file has a **unique identifier** to prevent conflicts.
- **Timestamps**: Tracks **creation** and **modification** times of the file.
- **Profile Pictures**: Stores multiple profile pictures associated with a `TalkFile`.
- **Tags**: Allows categorization of files with **custom tags**.
- **Checksum**: Ensures file integrity using **SHA-256** hash.

## File Format

The `.talk` file format contains the following fields:

| Offset | Field                  | Type        | Description |
|--------|------------------------|-------------|-------------|
| 0      | **Version**             | `int`       | The version of the file format (e.g., v1, v2). |
| 4      | **UUID**                | `byte[16]`  | Unique identifier for the file (introduced in v2). |
| 20     | **Created Timestamp**   | `long`      | When the file was created (introduced in v2). |
| 28     | **Modified Timestamp**  | `long`      | Last modification time (introduced in v2). |
| 36     | **Name Length**         | `int`       | Length of the name string. |
| 40     | **Name**                | `String`    | Name of the `TalkFile`. |
| N      | **Sound Count**         | `int`       | Number of sounds stored in the file. |
| N+4    | **Sounds**              | `byte[]`    | Sound data and associated probabilities. |
| N+X    | **Profile Picture Count** | `int`     | Number of profile pictures. |
| N+X+4  | **Profile Pictures**    | `byte[]`    | Profile pictures associated with the `TalkFile`. |
| N+Y    | **Tag Count**           | `int`       | Number of tags. |
| N+Y+4  | **Tags**                | `String[]`  | List of tags for categorizing the `TalkFile`. |
| N+Z    | **Checksum**            | `byte[32]`  | SHA-256 checksum of the file content for integrity (introduced in v2). |

## Versioning

The `.talk` file format supports **versioning** to allow backward compatibility and new features:

- **Version 1**:
    - Supports basic fields like name, sounds, profile pictures.
    - Does not support UUIDs, timestamps, tags, or checksum.
- **Version 2** (Current):
    - Adds **UUID** for unique file identification.
    - Adds **timestamps** for creation and modification times.
    - Allows multiple **profile pictures**.
    - Adds **tags** for categorization.
    - Implements a **checksum** to ensure file integrity. **NOTE: THIS FEATURE IS CURRENTLY DISABLED DUE TO MALFUNCTION, I AM WORKING ON A FIX**

### Version 1 File Format:

The first version of the `.talk` file format does not include UUIDs, timestamps, tags, or checksum. It simply contains the name, sounds, and profile pictures.

### Version 2 File Format:

Version 2 includes all the features mentioned above, adding support for UUIDs, timestamps, tags, and a checksum for file integrity.

## Methods

### `TalkFileManager`

- **saveTalkFile(TalkFile talkFile)**: Saves a `TalkFile` object to a `.talk` file. The version of the file is automatically set when saving.
- **loadTalkFile(String fileName)**: Loads a `TalkFile` from a `.talk` file. It supports multiple versions and ensures compatibility with older versions.

### `TalkFile`

The `TalkFile` class represents the structure of the `.talk` file. It contains the following attributes:

- **UUID**: A unique identifier for each file.
- **Created Timestamp**: The creation time of the file.
- **Modified Timestamp**: The last modification time of the file.
- **Name**: The name of the `TalkFile`.
- **Sounds**: A list of sound data (with associated probabilities).
- **Profile Pictures**: A list of profile pictures.
- **Tags**: A list of tags for categorization.
- **Checksum**: A SHA-256 checksum for file integrity.

## Usage

### Creating a `TalkFile`

```java
TalkFile talkFile = new TalkFile("exampleFile");
talkFile.addSound(soundData1, 0.9f);
talkFile.addSound(soundData2, 0.7f);
talkFile.addProfilePicture(profilePicture);
talkFile.addTag("robot");
talkFile.addTag("calm");
```
### Saving a TalkFile

```java
TalkFileManager talkFileManager = new TalkFileManager(new File("storage"));
talkFileManager.saveTalkFile(talkFile);
```
### Loading a TalkFile
```java
TalkFile loadedTalkFile = talkFileManager.loadTalkFile("exampleFile");
```
Handling File Integrity

When loading a TalkFile, the checksum is verified automatically. If the file has been corrupted or altered, an exception will be thrown.
```java
try {
    TalkFile talkFile = talkFileManager.loadTalkFile("exampleFile");
} catch (TalkFileException e) {
    System.out.println("Error loading the file: " + e.getMessage());
}
```
Exceptions

    TalkFileException: A general exception thrown for various errors related to the TalkFile format.

    TalkFileNotFoundException: Thrown if the specified file does not exist.

    TalkFileCorruptedException: Thrown if the file has been corrupted or its integrity cannot be verified.



