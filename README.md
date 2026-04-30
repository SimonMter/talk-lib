# talk-lib v3

A modern, extensible binary file format and Java library for storing and managing **audio-based personality data** (sounds, metadata, images, and tags).

---

# 🚀 Overview

`talk-lib` provides a structured way to store and load `.talk` files with:

* 🎵 Audio samples + probabilities
* 🖼 Profile pictures
* 🏷 Tags for categorization
* 🧾 Metadata (UUID, timestamps, name)
* 🔐 File integrity via SHA-256 checksum
* 🔄 Forward-compatible versioning

---

# 🛠 Usage

### 1. Create a TalkFile
```java
TalkFile tf = new TalkFile("My Personality");

// Add sounds with a probability (0.0 to 1.0)
tf.addSound(new byte[]{0x01, 0x02, 0x03}, 0.85f);

// Add tags
tf.addTag("robot");
tf.addTag("friendly");

// Add profile pictures
tf.addProfilePicture(someImageData);
```

### 2. Save to File
```java
TalkFileManager manager = new TalkFileManager();
File file = new File("mydata.talk");

try {
    manager.save(file, tf);
} catch (IOException e) {
    e.printStackTrace();
}
```

### 3. Load from File
```java
try {
    TalkFile loaded = manager.load(file);
    System.out.println("Loaded: " + loaded.getName());
} catch (TalkFileException e) {
    // Handle specific exceptions like TalkFileNotFoundException or TalkFileCorruptedException
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}
```

---

# 📦 File Format (v3)

## 🔑 Core Concept: Chunk-Based Structure

Instead of fixed offsets, the file is composed of **independent chunks**:

```
[ MAGIC ][ VERSION ][ CHUNKS... ][ CHECKSUM ]
```

---

## 🧱 Header

| Field   | Type    | Description             |
| ------- | ------- | ----------------------- |
| Magic   | 4 bytes | `"TALK"` identifier     |
| Version | int     | Format version (e.g. 3) |

---

## 🧩 Chunk Format

Each chunk follows:

```
4 bytes  → Chunk ID (ASCII, padded with spaces to 4 bytes)
4 bytes  → Chunk Size (int)
N bytes  → Data
```

### Supported Chunk IDs:

| ID     | Purpose          |
| ------ | ---------------- |
| `META` | Metadata         |
| `SND ` | Sounds           |
| `IMG ` | Profile Pictures |
| `TAG ` | Tags             |

---

## 📄 Chunk Definitions

### `META` (Metadata)

```
UUID (16 bytes)
Created Timestamp (long)
Modified Timestamp (long)
Name Length (int)
Name (UTF-8)
```

---

### `SND ` (Sounds)

```
Sound Count (int)

For each sound:
    Data Length (int)
    Probability (float)
    Data (byte[])
```

---

### `IMG ` (Profile Pictures)

```
Image Count (int)

For each image:
    Data Length (int)
    Data (byte[])
```

---

### `TAG ` (Tags)

```
Tag Count (int)

For each tag:
    Length (int)
    UTF-8 String
```

---

## 🔐 Checksum

* Algorithm: **SHA-256**
* Covers: **everything except the checksum itself** (Magic, Version, and all Chunks)
* Size: 32 bytes (appended at the end)

---

# ⚠️ Error Handling

The library uses a custom exception hierarchy for more granular error handling:

* `TalkFileException` (Base checked exception)
    * `TalkFileNotFoundException`: Thrown when a file cannot be located.
    * `TalkFileCorruptedException`: Thrown when the checksum verification fails or the file structure is invalid.
    * `TalkFileFormatException`: Thrown when the file magic identifier is incorrect.
    * `TalkFileVersionException`: Thrown when an unsupported file version is encountered.

---

# 🧪 Testing

To run the unit tests, use the following Gradle command:

```bash
./gradlew test
```

---

# 💡 Versioning Strategy

* The **file version only affects which chunks exist**
* Unknown chunks are **skipped automatically** during reading.
* This ensures both **backward** and **forward compatibility**.
