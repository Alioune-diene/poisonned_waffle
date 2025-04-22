# Waffle Game

## Overview
Waffle Game is a strategic board game where players take turns "eating" parts of a waffle. The player who eats the poisoned piece (the top-left corner) loses the game.

## Game Rules
1. The game is played on a rectangular waffle board (default size 6x8).
2. Players take turns selecting a piece of the waffle.
3. When a piece is selected, that piece and all pieces below and to the right of it are "eaten" (removed from the board).
4. The top-left corner (0,0) is poisoned - the player who eats this piece loses the game.
5. The goal is to force your opponent to eat the poisoned piece.

## Features
- Single-player mode with AI opponent (3 difficulty levels)
- Two-player mode
- Undo/Redo functionality
- Save/Load game state
- Hint system (limited to 3 hints per player)

## Technical Architecture
The game follows the Model-View-Controller (MVC) architectural pattern:

### Model (WaffleModel.java)
- Maintains the game state
- Handles game logic and rules
- Manages AI opponent through AIPlayer class
- Provides undo/redo functionality through move history

### View (WaffleGameView.java)
- Renders the game board
- Displays game information to players
- Captures user interactions

### Controller (WaffleController.java)
- Mediates between Model and View
- Processes user inputs
- Updates the Model and View as needed
- Manages AI moves with a realistic delay

## AI Implementation
The game features three AI difficulty levels:
1. **Easy (Level 1)**: Makes random valid moves
2. **Intermediate (Level 2)**: Uses basic strategy to make moves
3. **Advanced (Level 3)**: Uses sophisticated game analysis to make optimal moves

## How to Play
1. Launch the game
2. Choose game mode (single-player or two-player)
3. If playing against AI, select difficulty level
4. Click on any piece of the waffle to make a move
5. Try to force your opponent to eat the poisoned piece

## Controls
- Left-click on a waffle piece to make a move
- Use the buttons at the bottom to:
  - Start a new game
  - Undo/Redo moves
  - Get a hint
  - Save/Load game
  - Adjust AI settings

## File Format
Game states are saved with the `.waf` extension, which contains a serialized version of the WaffleModel object.
