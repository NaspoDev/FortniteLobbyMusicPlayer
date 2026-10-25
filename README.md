# Fortnite Lobby Music Player

A program that detects when you're in the Fortnite lobby and plays your desired music.

# Why?
I'm an OG Fortnite player and I wanted to set my lobby music to the classic Fortnite soundtrack.\
I was surprised to see that I didn't have it in my locker. This was because in Chapter 1 Season 10, for some\
unimaginable reason, Epic stupidly decided to put the original game soundtrack in the battle pass, making it forever\
exclusive to those who purchased the battle pass back then.

I had purchased every battle pass in Chapter 1 except for the one in season 10, because that was the one of the worst\
seasons in Fortnite history, and is where I quit the game for a while.

I can't believe that Epic Games would paywall and exclusivize the original game soundtrack. For crying out loud\
its music! Your game's classic soundtrack!

So, I decided to make this application to be able to play the classic game soundtrack in my lobby.\
(And it can technically play any other music too).

# How It Works
The application will take a screenshot of your main screen every second, and process the image with an
OCR to determine when you are in the lobby.

It determines this by looking for text only found on the lobby, like the text from the navigation menu on top.
(Ex. "Play", "Shop", etc...).

Once it's determined that you are in the lobby, it will start playing the music.

*It's currently only a personal project that is built with the original game soundtrack.*

# Usage
1. Run the application from the command line...
2. Play Fortnite and watch it work!

# Tech Stack
## Tesseract OCR
Tesseract is the Optical Character Recognition library (not natively available to the JVM) which has been made 
available to the JVM via Tess4J.

- [Tesseract OCR](https://github.com/tesseract-ocr/tesseract) 
- [Tess4J Homepage](https://tess4j.sourceforge.net/)
- [Tess4J Maven Repo](https://mvnrepository.com/artifact/net.sourceforge.tess4j/tess4j)