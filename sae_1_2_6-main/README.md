# SAE 2 : 1-2-6 : GROUPE 4 : Quoridor

## Students :

- **DUBREUIL** Timon (commit name : tdubreui)
- **MARECHAL** Nathan (commit name : marechal nathan, Natflaz)
- **PETERSCHMITT** Matthieu (commit name : peterschmitt matthieu, unurled)
- **TABOADA** Noé (commit name : nt579176)
- **VIEUX-MELCHIOR** Victor (commit name : vieux-melchior victor, TheGreatestRock)

## How to play :

### Launching the game :

The game have some arguments that you can use tu customize the game.

- ` ` (nothing) : Launch the game with the default settings. (2 players)
- `0 1` : Launch the game with 1 player and 1 AI (player first and coinflip ai)
- `0 2` : Launch the game with 1 player and 1 AI (player first and minimax ai)
- `1 2` : Launch the game with 2 AI (coinflip ai and minimax ai)

Exemple launch with a txt file : `0 0 < txt.txt` in idea (you need to set the correct path to the txt file)

### Playing the game :

The game is played with keyboard inputs typed in the terminal.

```text
USAGE :

"P{DIRECTION}" : "P" to select the pawn
                 "{DIRECTION}" a direction in wich you want to move the pawn 
                 Example : "PN" makes the pawn go to the north
                 
                 
if you jump over the opponent pawn and the cell behind the opponent pawn is a barrier, you can jump to the side of the opponent pawn using :
 "P{DIRECTION_1}{DIRECTION_2}" : "P" to select the pawn
                             "{DIRECTION_1}" the direction in which the opponent pawn is 
                             "{DIRECTION_2}" the direction you want the pawn to go after the jump
                             Example : "PNE" makes the pawn go to the north-east
                 
                 
 "W{COL}{ROW}{DIRECTION}" : "W" to select a wall
                           "{COL}" in form of a letter (ref. at the bottom of the board)
                           "{ROW}" in form of a number (ref. at the left of the board)
                           "{DIRECTION}" a direction "H" or "V" respectively for horizontal or vertical
                           You select the center of the barrier.
                           Example : "WA2H"


"STOP" : to stop/end the game
```

