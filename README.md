# Fortnite Lobby Music Player [IN DEVELOPMENT]

A program that detects when you're in the Fortnite lobby and plays your desired music.

# Why?

I'm an OG Fortnite player and I wanted to set my lobby music to the classic Fortnite soundtrack.\
I was surprised to see that I didn't have it in my locker. This was because in Chapter 1 Season 10, for some\
unimaginable reason, Epic decided to put the original game soundtrack in the battle pass, making it forever\
exclusive to those who purchased the battle pass back then.

I had purchased every battle pass in Chapter 1 except for the one in season 10, because that was the one of the worst\
seasons in Fortnite history, and is where I quit the game for a while.

I can't believe that Epic Games would paywall and exclusivize the original game soundtrack. For crying out loud\
its music! Your game's classic soundtrack!

Anyways, I decided to make this application to be able to play the classic game soundtrack in my lobby.\
(And it can technically play any other music too).

# How It Works

The application will take a screenshot of your main screen every second, and process the image with an
OCR (Optical Character Recognition) engine to determine when you are in the lobby.

As a bonus, it will also detect when you're in the shop and the locker, and will pause music playback
as to not interfere with things like emotes and other sound effects from these screens.

It determines your game's state by looking for text only found on those screens.\
For example, the text from the navigation menu on top.
(Ex. "Play", "Shop", etc...).

Once it's determined that you are in the lobby, it will start playing the music.

_It's currently only a personal project that is built with the original game soundtrack._

## Notable Optimization

Instead of taking a full screenshot of the entire screen, two screenshots of a small sliver of the top
and bottom of the screen are taken.\
These two images are processed in parallel using multi-threading.

The smaller screen captures are the only portions of the screen that are necessary for this program, and is
much less work for the OCR engine to process compared to the whole screen.

The two smaller images, combined with parallel processing bring the time per OCR process from **2.7 seconds**
down to **130 milliseconds**.

# Usage

**Requirements:**

- Runs on Windows only.
- Java installed on the system.

**Steps:**

1. Build the app with `./gradlew build`.
2. Run the build output (the jar) with `java -jar <output_file>`.
3. Play Fortnite and watch it work!

# Libraries Used

## Tesseract OCR

Tesseract is the Optical Character Recognition library (not natively available to the JVM) which has been made
available to the JVM via Tess4J.

- [Tesseract OCR](https://github.com/tesseract-ocr/tesseract)
- [Tess4J Homepage](https://tess4j.sourceforge.net/)
- [Tess4J Maven Repo](https://mvnrepository.com/artifact/net.sourceforge.tess4j/tess4j)

## kotlin-logging

[kotlin-logging](https://github.com/oshai/kotlin-logging#) for logging, using [slf4j-simple](https://mvnrepository.com/artifact/org.slf4j/slf4j-simple) as its backend.
