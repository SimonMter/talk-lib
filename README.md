# talk-lib v3

A modern, extensible binary file format and Java library for storing and managing **audio-based personality data** (sounds, metadata, images, and tags).

---

# 🚀 Overview

`talk-lib` provides a structured way to store and load `.talk` files with:

* 🎵 Audio samples + probabilities
* 🖼 Profile pictures
* 🏷 Tags for categorization
* 🧾 Metadata (UUID, timestamps, name)
* 🔐 File integrity via checksum
* 🔄 Forward-compatible versioning

This version (v3) introduces a **chunk-based binary format**, making files easier to extend, debug, and maintain.

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
4 bytes  → Chunk ID (ASCII)
4 bytes  → Chunk Size (int)
N bytes  → Data
```

### Example Chunk IDs:

| ID   | Purpose          |
| ---- | ---------------- |
| META | Metadata         |
| SND  | Sounds           |
| IMG  | Profile Pictures |
| TAGS | Tags             |

---

## 📄 Chunk Definitions

### META (Metadata)

```
UUID (16 bytes)
Created Timestamp (long)
Modified Timestamp (long)
Name Length (int)
Name (UTF-8)
```

---

### SND (Sounds)

```
Sound Count (int)

For each sound:
    Data Length (int)
    Probability (float)
    Data (byte[])
```

---

### IMG (Profile Pictures)

```
Image Count (int)

For each image:
    Data Length (int)
    Data (byte[])
```

---

### TAGS

```
Tag Count (int)

For each tag:
    Length (int)
    UTF-8 String
```

---

## 🔐 Checksum

* Algorithm: **SHA-256**
* Covers: **everything except the checksum itself**
* Size: 32 bytes (appended at the end)

---

# 🧠 Versioning Strategy

* The **file version only affects which chunks exist**
* Unknown chunks are **skipped automatically**

👉 This allows:

* Backward compatibility
* Forward compatibility

---

# 🛠 Java API Design

## `TalkFile`

```java
class TalkFile {
    UUID uuid;
    long created;
    long modified;
    String name;

    List<SoundEntry> sounds;
    List<byte[]> images;
    List<String> tags;
}
```

---

## `SoundEntry`

```java
class SoundEntry {
    byte[] data;
    float probability;
}
```

---

## `TalkFileManager`

### Save

```java
void save(File file, TalkFile talkFile);
```

### Load

```java
TalkFile load(File file);
```

---

# 🧭 Implementation Roadmap

## Phase 1 — Project Setup

1. Create project structure:

```
talk-lib/
 ├── model/
 ├── io/
 ├── util/
 └── test/
```

2. Add core classes:

* `TalkFile`
* `SoundEntry`
* `TalkFileManager`

---

## Phase 2 — Binary Writer (Start Here)

👉 Implement writing BEFORE reading.

### Steps:

1. Open `ByteArrayOutputStream`
2. Wrap with `DataOutputStream`
3. Write:

  * Magic `"TALK"`
  * Version `3`
4. Write chunks:

  * META
  * SND
  * IMG
  * TAGS
5. Convert to byte array
6. Compute SHA-256 checksum
7. Append checksum
8. Write to file

---

## Phase 3 — Binary Reader

### Steps:

1. Read file into byte array
2. Extract last 32 bytes → checksum
3. Verify checksum
4. Wrap remaining data in `DataInputStream`
5. Read:

  * Magic (validate)
  * Version
6. Loop through chunks:

```java
while (input.available() > 0) {
    read chunkId
    read size
    read data

    switch(chunkId) {
        case "META": parseMeta(...)
        case "SND": parseSounds(...)
        case "IMG": parseImages(...)
        case "TAGS": parseTags(...)
        default: skip
    }
}
```

---

## Phase 4 — Checksum (Important Fix)

### Correct Implementation:

#### Writing:

* Compute hash AFTER writing all data
* Do NOT include checksum field

#### Reading:

* Remove last 32 bytes
* Recompute hash
* Compare

---

## Phase 5 — Testing

Create test cases:

* ✅ Empty file
* ✅ File with 1 sound
* ✅ Large file (many sounds/images)
* ✅ Corrupted file (truncate bytes)
* ✅ Unknown chunk (simulate future version)

---

## Phase 6 — Debug Tool (Highly Recommended)

Build a CLI tool:

```
talk inspect file.talk
talk extract file.talk
```

This helps:

* Debug corrupted files
* Validate structure
* Inspect chunks

---

## Phase 7 — Optional Improvements

### Compression

* Compress SND + IMG chunks (GZIP)

### Streaming

* Load sounds lazily for large files

### Encryption (optional)

* Encrypt chunks if needed

---

# ⚡ Getting Started (Step-by-Step)

## 1. Start with Writer

Create:

```
io/TalkFileWriter.java
```

Goal:
✔ Write a valid `.talk` file (no checksum first)

---

## 2. Add Reader

Create:

```
io/TalkFileReader.java
```

Goal:
✔ Load what you wrote

---

## 3. Add Checksum

Goal:
✔ Detect corrupted files

---

## 4. Refactor into Manager

Combine into:

```
TalkFileManager
```

---

## 5. Add Tests

Make sure:
✔ Load == Save
✔ Corruption detected

---

# ⚠️ Common Mistakes

❌ Mixing read/write order
❌ Forgetting string encoding (use UTF-8!)
❌ Including checksum in hash
❌ Not validating lengths before reading arrays
❌ Hardcoding offsets

---

# 💡 Final Notes

* Keep the format **simple but structured**
* Don’t over-optimize early
* Chunk-based design = future-proof

---

# 📌 Next Steps

If you want, I can:

* Generate a **full Java implementation (writer + reader)**
* Help you **fix your current checksum bug**
* Add **compression support properly**
* Or design a **v4 with streaming support**

