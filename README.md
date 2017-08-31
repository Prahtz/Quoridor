# Quoridor
Implementation of the board game, made for software programming course at UNIFI.

**To run the game**, do as follow:
1. Import this Java project on Eclipse
2. Enter lib and right click on *quoridorgui.jar* or *quoridor.jar*
3. Select *Build Path -> Add to Build Path*

If you want **to run a set of matches** on the console, do as follow:
1. Right click on the Java project
2. Select *Run As -> Run Configurations...*
3. In the *"Main"* tab select the quoridor project and for the *"Main class"* select gj.quoridor.engine.Quoridor
4. In the *"Arguments"* tab insert *<number_of_matches> <first_player_name> <second_player_name> <true/false>* (true stands for verbose mode)
5. Click *Apply* or *Run* in the bottom right corner of the window

If you want **to play human vs ai**, do as follow:
1. Right click on the Java project
2. Select *Run As -> Run Configurations...*
3. In the *"Main"* tab select the quoridor project and for the *"Main class"* select *gj.quoridor.engine.QuoridorGUI*
4. In the *"Arguments"* tab insert *<ai_player_name>*
5. Click *Apply* or *Run* in the bottom right corner of the window

##### Notes: If you want to play with my ai player just insert *Falai* in *<ai_player_name>* space
