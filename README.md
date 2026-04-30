# talk-lib v3

This is the number #1 library to interact with `.talk` data files.

This library was developed to be used in the Android application **"Sprechende Respektspersonen"** by **SJ-DEV-DYNAMICS**.

---

# Overview

`talk-lib` is a Java-based binary serialization library designed for storing and loading structured `.talk` files. These files are used to represent audio-driven personality data, including sounds, metadata, images, and tags.

The format is optimized for performance, extensibility, and data integrity.

---

# Features

- Audio samples with probability weighting
- Profile image storage
- Tag-based categorization
- Structured metadata (UUID, timestamps, name)
- SHA-256 checksum integrity validation
- Chunk-based binary format
- Strict version control
- Custom exception hierarchy for precise error handling

---

# Usage

## Create a TalkFile

```java
TalkFile tf = new TalkFile("My Personality");

// Add sound samples
tf.addSound(new byte[]{0x01, 0x02, 0x03}, 0.85f);

// Add tags
tf.addTag("robot");
tf.addTag("friendly");

// Add profile picture
tf.addProfilePicture(imageBytes);
```

## Save a TalkFile

```java
TalkFileManager manager = new TalkFileManager();
File file = new File("mydata.talk");

try {
    manager.save(file, tf);
} catch (TalkFileException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
```

## Load a TalkFile

```java
try {
    TalkFile loaded = manager.load(file);
    System.out.println("Loaded: " + loaded.getName());
} catch (TalkFileException e) {
    // Handles format, corruption, version, and missing file errors
    e.printStackTrace();
}
```

---

# File Format (v3)

## Structure Overview

A `.talk` file is composed of:

```
[ MAGIC ][ VERSION ][ CHUNKS... ][ CHECKSUM ]
```

## Header

| Field | Type | Description |
| :--- | :--- | :--- |
| MAGIC | 4 bytes | Fixed identifier: "TALK" |
| VERSION | int | File format version |

## Chunk Format

Each chunk follows a strict structure:

```
4 bytes  → Chunk ID (ASCII, padded to 4 bytes)
4 bytes  → Chunk size (int)
N bytes  → Chunk data
```

## Supported Chunk Types

| ID | Description |
| :--- | :--- |
| META | Metadata |
| SND | Sound data |
| IMG | Profile images |
| TAG | Tags |

## Chunk Details

### META
- UUID (16 bytes)
- Created timestamp (long)
- Modified timestamp (long)
- Name length (int)
- Name (UTF-8 string)

### SND
Sound count (int)

For each sound:
- Data length (int)
- Probability (float)
- Raw sound bytes

### IMG
Image count (int)

For each image:
- Data length (int)
- Image bytes

### TAG
Tag count (int)

For each tag:
- Length (int)
- UTF-8 string

## Checksum

- **Algorithm**: SHA-256
- **Scope**: all data except the checksum field itself
- **Size**: 32 bytes
- **Purpose**: ensures file integrity and detects corruption

---

# Error Handling

The library uses a structured exception hierarchy:

### Base Exception
- `TalkFileException`

### Specific Exceptions

| Exception | Description |
| :--- | :--- |
| `TalkFileNotFoundException` | File does not exist or cannot be accessed |
| `TalkFileCorruptedException` | Checksum mismatch or data corruption detected |
| `TalkFileFormatException` | Invalid structure, chunk format, or magic header |
| `TalkFileVersionException` | Unsupported file version encountered |

---

# Testing

Run all unit tests:

```bash
./gradlew test
```

Recommended test coverage includes:
- Stress tests with large datasets
- Corruption handling
- Randomized fuzz testing
- Serialization integrity validation

---

# Versioning Strategy
- Each file contains a version field in the header
- Future versions may add or modify chunk types
- Unknown chunks are ignored during parsing
- Backward compatibility is preserved through chunk-based parsing

# Design Goals
- Deterministic binary serialization
- High performance file IO
- Clear separation of data chunks
- Robust corruption detection
- Extensible format without breaking changes
