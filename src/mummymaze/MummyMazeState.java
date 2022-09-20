package mummymaze;

import agent.Action;
import agent.State;
import eightpuzzle.EightPuzzleState;
import mummymaze.arrays.Door;
import mummymaze.arrays.Mob;
import mummymaze.arrays.Trap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;


public class MummyMazeState extends State implements Cloneable {

    public static final int SIZE = 13;
    private final char[][] matrix;

    // Movement Variables
    private char[][] priorityQueue;

    // Hero Variables
    private int heroLine;
    private int heroColumn;

    // Mobs Variables
    private Mob[] Mobs;
    private int mobsCounter;

    private LinkedList<Integer> MobsSoftDelete;
    private int[] DeletedList;
    private int deletedCounter;

    // Mob Counters
    private int wMummyCounter;
    private int rMummyCounter;
    private int ScorpionCounter;

    // Key Variables
    private int KeyLine;
    private int KeyColumm;

    // Door Variables
    private Door[] Doors;
    private int doorCounter;


    // Trap Variables
    private LinkedList<Trap> Traps;
    private int trapCounter;

    // Goal Variables
    private int GoalLine;
    private int GoalColumm;

    // Dead End Reached Variable
    private boolean dead;


    public MummyMazeState(char[][] matrix, int keyLine, int keyColumm, LinkedList<Trap> traps) {
        dead=false;
        doorCounter=0;
        mobsCounter=0;
        trapCounter=0;
        deletedCounter=0;
        KeyLine=0;
        this.Doors = new Door[10]; // Maximo 10 Portas
        this.Mobs = new Mob[5];    // Maximo 5 Mobs
        this.Traps = traps;   // Maximo 5 Traps
        this.MobsSoftDelete = new LinkedList<>();
        this.DeletedList= new int[5];
        this.KeyLine = keyLine;
        this.KeyColumm = keyColumm;

        this.matrix = new char[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                this.matrix[i][j] = matrix[i][j];
                switch (this.matrix[i][j]){
                    case 'H' :              // Hero
                        heroLine = i;
                        heroColumn = j;
                        break;
                    case 'M' :              // White Mummy
                        Mobs[mobsCounter] = new Mob('w',i,j);
                        mobsCounter++;
                        break;
                    case 'V' :              // Red Mummy
                        Mobs[mobsCounter] = new Mob('r',i,j);
                        mobsCounter++;
                        break;
                    case 'E' :              // Scorpion
                        Mobs[mobsCounter] = new Mob('s',i,j);
                        mobsCounter++;
                        break;
                    case 'S' :              // Goal
                        GoalLine = i;
                        GoalColumm = j;
                        break;

                    case '=' :              //Walls ( closed to open , hor - ver )
                    case '_' :
                    case '"' :
                    case ')' :
                        Doors[doorCounter] = new Door(i,j);
                        doorCounter++;
                        break;

                }

                if(GoalLine==0){
                    GoalLine++;
                }
                if(GoalLine==12){
                    GoalLine--;
                }
                if(GoalColumm==0){
                    GoalColumm++;
                }
                if(GoalColumm==12){
                    GoalColumm--;
                }
                /*if (heroLine == 0 && heroColumn == 0) {
                    dead = true;
                    break;
                }*/
                // Re-Locating Goal ( Only One Space Between Goal Position and Tile )


                /*if ((heroLine == lineRedMummy) && (columnHero == columnRedMummy)) {
                    dead = true;
                    break;
                }*/


                // Do if for Saida e Mummia
            }

        }
        if ((heroLine == 0) && (heroColumn == 0) || isDead()) {
            dead = true;
        }

    }



    public boolean isDead(){
        return dead;
    }


    @Override
    public void executeAction(Action action) {
        action.execute(this);
        firePuzzleChanged(null);
    }

    // Gets

    public int getHeroLine() {
        return heroLine;
    }
    public int getHeroColumn() {
        return heroColumn;
    }
    public int getGoalLine() {
        return GoalLine;
    }
    public int getGoalColumm() {
        return GoalColumm;
    }


    // Mob Controllers

    public void MobMovements(){
        char type= ' ';
        if(Mobs[0]!=null) {
            for (int i = 0; i < mobsCounter; i++) {
                if(MobIsDead(i)){
                    continue;
                }
                if(dead==true){
                    break;
                }
                type = Mobs[i].getType();
                switch (type) {
                    case 'w':
                        WMummyMovements(Mobs[i].getX(), Mobs[i].getY(), i);
                        break;
                    case 'r':
                        RMummyMovements(Mobs[i].getX(), Mobs[i].getY(), i);
                        break;
                    case 's':
                        ScorpionMovements(Mobs[i].getX(), Mobs[i].getY(), i);
                        break;
                }
            }
        }
    }

    public void WMummyMovements(int x,int y,int index){
        wMummyCounter=0;
        Mobs[index].setAction(false);
        do{
            // Check Right Then Up Down
            if(heroColumn>y){
                if (wcanMoveRight(x, y)) {
                    wMummymoveRight(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    wMummyCounter++;
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(wMummyCounter==2){break;}
                } else if (wcanMoveUp(x, y)) {
                    wMummymoveUp(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    wMummyCounter++;
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(wMummyCounter==2){break;}
                }else if (wcanMoveDown(x, y)) {
                    wMummymoveDown(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    wMummyCounter++;
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(wMummyCounter==2){break;}
                }
            }

            // Check Left Then Up Down
            if(heroColumn<y){
                if (wcanMoveLeft(x, y)) {
                    wMummymoveLeft(index);
                    wMummyCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(wMummyCounter==2){break;}
                } else if (wcanMoveUp(x, y)) {
                    wMummymoveUp(index);
                    wMummyCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(wMummyCounter==2){break;}
                }else if (wcanMoveDown(x, y)) {
                    wMummymoveDown(index);
                    wMummyCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(wMummyCounter==2){break;}
                }
            }

            // Check Up and Down
            if(heroColumn==y){
                if (wcanMoveUp(x, y)) {
                    wMummymoveUp(index);
                    wMummyCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(wMummyCounter==2){break;}
                }else if (wcanMoveDown(x, y)) {
                    wMummymoveDown(index);
                    wMummyCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(wMummyCounter==2){break;}
                }
            }
           // firePuzzleChanged(null);         // Remove For Speed Of Show Solution // Extra For Mob Only Moving Visually One Tile Per Turn
            if(Mobs[index].getAction()){
                Mobs[index].setAction(false);
            }else{
                wMummyCounter++;
            }
        }while(wMummyCounter<2);
    }

    public void RMummyMovements(int x,int y,int index){
        rMummyCounter=0;
        Mobs[index].setAction(false);
        do{
            // Check Down Then Right Left
            if(heroLine>x){
                if (rcanMoveDown(x, y)) {
                    rMummymoveDown(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    rMummyCounter++;
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(rMummyCounter==2){break;}

                }else if (rcanMoveRight(x, y)) {
                    rMummymoveRight(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    rMummyCounter++;
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(rMummyCounter==2){break;}

                } else if (rcanMoveLeft(x, y)) {
                    rMummymoveLeft(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    rMummyCounter++;
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(rMummyCounter==2){break;}
                }
            }

            // Check Up then Right Left
            if(heroLine<x){
                if (rcanMoveUp(x, y)) {
                    rMummymoveUp(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    rMummyCounter++;
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(rMummyCounter==2){break;}

                }else if (rcanMoveRight(x, y)) {
                    rMummymoveRight(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    rMummyCounter++;
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(rMummyCounter==2){break;}

                } else if (rcanMoveLeft(x, y)) {
                    rMummymoveLeft(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    rMummyCounter++;
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(rMummyCounter==2){break;}
                }
            }

            // Check Up and Down
            if(heroLine==x){
                if (rcanMoveRight(x, y)) {
                    rMummymoveRight(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    rMummyCounter++;
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(rMummyCounter==2){break;}

                } else if (rcanMoveLeft(x, y)) {
                    rMummymoveLeft(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    rMummyCounter++;
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(rMummyCounter==2){break;}
                }
            }
           // firePuzzleChanged(null);         // Remove For Speed Of Show Solution // Extra For Mob Only Moving Visually One Tile Per Turn
            if(Mobs[index].getAction()){
                Mobs[index].setAction(false);
            }else{
                rMummyCounter++;
            }
        }while(rMummyCounter<2);
    }

    public void ScorpionMovements(int x,int y,int index){
        ScorpionCounter=0;
        Mobs[index].setAction(false);
        do{
            // Check Right Then Up Down
            if(heroColumn>y){
                if (scpcanMoveRight(x, y)) {
                    ScorpionmoveRight(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    ScorpionCounter++;
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(ScorpionCounter==1){break;}
                } else if (scpcanMoveUp(x, y)) {
                    ScorpionmoveUp(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    ScorpionCounter++;
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(ScorpionCounter==1){break;}
                }else if (scpcanMoveDown(x, y)) {
                    ScorpionmoveDown(index);
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    ScorpionCounter++;
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(ScorpionCounter==1){break;}
                }
            }

            // Check Left Then Up Down
            if(heroColumn<y){
                if (scpcanMoveLeft(x, y)) {
                    ScorpionmoveLeft(index);
                    ScorpionCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    y = Mobs[index].getY();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(ScorpionCounter==1){break;}
                } else if (scpcanMoveUp(x, y)) {
                    ScorpionmoveUp(index);
                    ScorpionCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(ScorpionCounter==1){break;}
                }else if (scpcanMoveDown(x, y)) {
                    ScorpionmoveDown(index);
                    ScorpionCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(ScorpionCounter==1){break;}
                }
            }

            // Check Up and Down
            if(heroColumn==y){
                if (scpcanMoveUp(x, y)) {
                    ScorpionmoveUp(index);
                    ScorpionCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(ScorpionCounter==1){break;}
                }else if (scpcanMoveDown(x, y)) {
                    ScorpionmoveDown(index);
                    ScorpionCounter++;
                    if(MobIsDead(index)){
                        break;
                    }
                    Mobs[index].setAction(true);
                    x = Mobs[index].getX();
                    if(heroLine==x && heroColumn==y){
                        dead=true;
                        break;
                    }
                    if(ScorpionCounter==1){break;}
                }
            }
           // firePuzzleChanged(null);        // Remove For Speed Of Show Solution // Extra For Mob Only Moving Visually One Tile Per Turn
            if(Mobs[index].getAction()){
                Mobs[index].setAction(false);
            }else{
                ScorpionCounter++;
            }
        }while(ScorpionCounter<1);

    }

    /*
     * In the next four methods we don't verify if the actions are valid.
     * This is done in method executeActions in class EightPuzzleProblem.
     * Doing the verification in these methods would imply that a clone of the
     * state was created whether the operation could be executed or not.
     */




    // Hero
    public void noMove(){
        /*if(isDead()){
            return;
        }*/
        matrix[heroLine][heroColumn]='H';
        MobMovements();
    }
    public void moveUp() {

        /*if(isDead()){
            return;
        }*/
      /*  if(matrix[heroLine-1][heroColumn]=='S'){
            heroLine--;
            matrix[heroLine][heroColumn]= 'H';
            return;
        }*/
        if(isKey(heroLine,heroColumn)){

            matrix[heroLine][heroColumn] = 'C';

        }else{
            matrix[heroLine][heroColumn] = '.';
        }

            heroLine -= 2;

            matrix[heroLine][heroColumn] = 'H';

        if(isKey(heroLine,heroColumn)) {
            keyPressed();
        }

       MobMovements();
    }
    public void moveRight() {

       /* if(isDead()){
            return;
        }*/
        if(isKey(heroLine,heroColumn)){

            matrix[heroLine][heroColumn] = 'C';

        }else{
            matrix[heroLine][heroColumn] = '.';
        }


            heroColumn += 2;

        matrix[heroLine][heroColumn] = 'H';

        if(isKey(heroLine,heroColumn)) {
            keyPressed();
        }


       MobMovements();
    }
    public void moveDown() {
        /*if(isDead()){
            return;
        }*/
        if(isKey(heroLine,heroColumn)){

            matrix[heroLine][heroColumn] = 'C';

        }else{
            matrix[heroLine][heroColumn] = '.';
        }


            heroLine += 2;

        matrix[heroLine][heroColumn] = 'H';

        if(isKey(heroLine,heroColumn)) {
            keyPressed();
        }

       MobMovements();
    }
    public void moveLeft() {
       /* if(isDead()){
            return;
        }*/
        if(isKey(heroLine,heroColumn)){
            matrix[heroLine][heroColumn] = 'C';

        }else{
            matrix[heroLine][heroColumn] = '.';
        }

            heroColumn -= 2;

        matrix[heroLine][heroColumn] = 'H';

        if(isKey(heroLine,heroColumn)) {
            keyPressed();
        }
            MobMovements();
    }


    // Hero Checks
    public boolean canMoveUp() {
        if((heroLine > 1) && (matrix[heroLine-1][heroColumn] == ' ' || matrix[heroLine-1][heroColumn] == '_') && (matrix[heroLine-2][heroColumn] == '.' || matrix[heroLine-2][heroColumn] == 'C')){

            return true;
        }
        return false;
    }
    public boolean canMoveRight() {
        if((heroColumn < 11)  && (matrix[heroLine][heroColumn+1]  == ' ' || matrix[heroLine][heroColumn+1]  == ')' ) && (matrix[heroLine][heroColumn+2] == '.' || matrix[heroLine][heroColumn+2] == 'C')){
            return true;
        }
        return false;
    }
    public boolean canMoveDown() {
        if((heroLine < 11) && (matrix[heroLine+1][heroColumn] == ' ' || matrix[heroLine+1][heroColumn] == '_') && (matrix[heroLine+2][heroColumn] == '.' || matrix[heroLine+2][heroColumn] == 'C')){
            return true;
        }
        return false;
    }
    public boolean canMoveLeft() {
        if((heroColumn > 1) && (matrix[heroLine][heroColumn-1]  == ' ' || matrix[heroLine][heroColumn-1]  == ')' ) && (matrix[heroLine][heroColumn-2] == '.' || matrix[heroLine][heroColumn-2] == 'C')){
            return true;
        }
        return false;
    }


    // White Mummy
    public void wMummymoveUp(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        } else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        } else {
            matrix[x][y] = '.';
        }

            x -= 2;

        matrix[x][y] = 'M';

        if(mobDeleteCheck(x,y,'w',index)){
            Mobs[index].setX(x);
            return;
        }

        if(isKey(x,y)) {
            keyPressed();
        }

        Mobs[index].setX(x);


    }
    public void wMummymoveRight(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        } else{
            matrix[x][y] = '.';
        }

            y += 2;

        matrix[x][y] = 'M';

        if(mobDeleteCheck(x,y,'w',index)){
            Mobs[index].setY(y);
            return;
        }
        if(isKey(x,y)) {
            keyPressed();
        }

        Mobs[index].setY(y);



    }
    public void wMummymoveDown(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        } else{
            matrix[x][y] = '.';
        }

            x += 2;

        matrix[x][y] = 'M';

        if(mobDeleteCheck(x,y,'w',index)){
            Mobs[index].setY(y);
            return;
        }
        if(isKey(x,y)) {
            keyPressed();
        }

        Mobs[index].setX(x);


    }
    public void wMummymoveLeft(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        } else{
            matrix[x][y] = '.';
        }

            y -= 2;

        matrix[x][y] = 'M';

        if(mobDeleteCheck(x,y,'w',index)){
            Mobs[index].setY(y);
            return;
        }
        if(isKey(x,y)) {

            keyPressed();
        }

        Mobs[index].setY(y);


    }


    // White Mummy Checks
    public boolean wcanMoveUp(int x,int y) {
            if(heroLine<x){
                if(matrix[x-1][y]  != '-' && matrix[x-1][y]  != '='){
                    return true;
                }

        }
        return false;
    }
    public boolean wcanMoveRight(int x,int y) {
        if (heroColumn>y){
            if(matrix[x][y+1]  != '"' && matrix[x][y+1]  != '|') {
                return true;
            }
        }
        return false;

    }
    public boolean wcanMoveDown(int x,int y) {
            if(heroLine>x){
                if(matrix[x+1][y]  != '-' && matrix[x+1][y]  != '='){
                    return true;
                }

        }
        return false;
    }
    public boolean wcanMoveLeft(int x,int y) {
        if (heroColumn<y){
            if(matrix[x][y-1]  != '"' && matrix[x][y-1]  != '|') {
                return true;
            }
        }
        return false;

    }



    // Red Mummy
    public void rMummymoveUp(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        }else{
            matrix[x][y] = '.';
        }

        x -= 2;

        matrix[x][y] = 'V';

        if(mobDeleteCheck(x,y,'r',index)){
            Mobs[index].setX(x);
            return;
        }

        Mobs[index].setX(x);

        if(isKey(x,y)){
            keyPressed();
        }

    }
    public void rMummymoveRight(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        }else{
            matrix[x][y] = '.';
        }

        y += 2;

        matrix[x][y] = 'V';

        if(mobDeleteCheck(x,y,'r',index)){
            Mobs[index].setY(y);
            return;
        }
        if(matrix[11][7]=='V'){
            dead = dead;
        }

        Mobs[index].setY(y);

        if(isKey(x,y)){
            keyPressed();
        }
    }
    public void rMummymoveDown(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        }else{
            matrix[x][y] = '.';
        }

        x += 2;

        matrix[x][y] = 'V';

        if(mobDeleteCheck(x,y,'r',index)){
            Mobs[index].setX(x);
            return;
        }

        Mobs[index].setX(x);

        if(isKey(x,y)){
            keyPressed();
        }
    }
    public void rMummymoveLeft(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        }else{
            matrix[x][y] = '.';
        }

        y -= 2;

        matrix[x][y] = 'V';

        if(mobDeleteCheck(x,y,'r',index)){
            Mobs[index].setY(y);
            return;
        }

        Mobs[index].setY(y);

        if(isKey(x,y)){
            keyPressed();
        }
    }


    // Red Mummy Checks
    public boolean rcanMoveUp(int x,int y) {
        if(heroLine<x){
                if(matrix[x-1][y]  != '-' && matrix[x-1][y]  != '='){
                    return true;
                }
        }
        return false;
    }
    public boolean rcanMoveRight(int x,int y) {
            if (heroColumn>y){
                if(matrix[x][y+1]  != '"' && matrix[x][y+1]  != '|') {
                    return true;
                }

        }
        return false;

    }
    public boolean rcanMoveDown(int x,int y) {
        if(heroLine>x){
            if(matrix[x+1][y]  != '-' && matrix[x+1][y]  != '='){
                return true;
            }
        }
        return false;
    }
    public boolean rcanMoveLeft(int x,int y) {

            if (heroColumn<y){
                if(matrix[x][y-1]  != '"' && matrix[x][y-1]  != '|') {
                    return true;
                }

        }
        return false;
    }


    // Scorpion
    public void ScorpionmoveUp(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        }else{
            matrix[x][y] = '.';
        }

        x -= 2;

        matrix[x][y] = 'E';

        if(mobDeleteCheck(x,y,'s',index)){
            Mobs[index].setX(x);
            return;
        }

        Mobs[index].setX(x);

        if(isKey(x,y)){
            keyPressed();
        }
    }

    public void ScorpionmoveRight(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        }else{
            matrix[x][y] = '.';
        }

        y += 2;

        matrix[x][y] = 'E';

        if(mobDeleteCheck(x,y,'s',index)){
            Mobs[index].setY(y);
            return;
        }

        Mobs[index].setY(y);

        if(isKey(x,y)){
            keyPressed();
        }
    }

    public void ScorpionmoveDown(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        }else{
            matrix[x][y] = '.';
        }

        x += 2;

        matrix[x][y] = 'E';

        if(mobDeleteCheck(x,y,'s',index)){
            Mobs[index].setX(x);
            return;
        }

        Mobs[index].setX(x);

        if(isKey(x,y)){
            keyPressed();
        }
    }

    public void ScorpionmoveLeft(int index) {
        int x = Mobs[index].getX();
        int y = Mobs[index].getY();

        if(isTrap(x,y)){
            matrix[x][y] = 'A';
        }else if (isKey(x, y)) {
            matrix[x][y] = 'C';
        }else{
            matrix[x][y] = '.';
        }

        y -= 2;

        matrix[x][y] = 'E';

        if(mobDeleteCheck(x,y,'s',index)){
            Mobs[index].setY(y);
            return;
        }

        Mobs[index].setY(y);

        if(isKey(x,y)){
            keyPressed();
        }
    }



    // Scorpion Checks
    public boolean scpcanMoveUp(int x,int y) {
            if(heroLine<x){
                if(matrix[x-1][y]  != '-' && matrix[x-1][y]  != '='){
                    return true;
                }

        }
        return false;
    }

    public boolean scpcanMoveRight(int x,int y) {
        if (heroColumn>y){
            if(matrix[x][y+1]  != '"' && matrix[x][y+1]  != '|') {
                return true;
            }
        }
        return false;

    }

    public boolean scpcanMoveDown(int x,int y) {
            if(heroLine>x){
                if(matrix[x+1][y]  != '-' && matrix[x+1][y]  != '='){
                    return true;
                }
            }

        return false;
    }

    public boolean scpcanMoveLeft(int x,int y) {
        if (heroColumn<y){
            if(matrix[x][y-1]  != '"' && matrix[x][y-1]  != '|') {
                return true;
            }
        }
        return false;

    }
    // Mob Collision Check

    public boolean mobDeleteCheck(int x,int y,char type,int index){
        if(mobsCounter!=0){
            for(int i=0;i<mobsCounter;i++){
                if(x==Mobs[i].getX() && y == Mobs[i].getY() ){
                    if(type=='w' || type =='r'){
                        switch (Mobs[i].getType()){
                            case 'r':
                                mobRemove(index);
                                matrix[x][y] = 'V';
                                return true;
                            case 'w':
                                mobRemove(index);
                                matrix[x][y] = 'M';
                                return true;
                            case 's':
                                if(type=='w'){
                                    matrix[x][y] = 'M';
                                }else{matrix[x][y] = 'V';}
                                mobRemove(i);
                                return false;
                        }
                    }else{
                        switch (Mobs[i].getType()){
                            case 'r':
                                mobRemove(index);
                                matrix[x][y] = 'V';
                                return true;
                            case 'w':
                                mobRemove(index);
                                matrix[x][y] = 'M';
                                return true;
                            case 's':
                                mobRemove(index);
                                matrix[x][y] = 'E';
                                return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    public void mobRemove(int index){
        //MobsSoftDelete.add(index);
        DeletedList[deletedCounter]=index;
        deletedCounter++;
        /*Mob[] proxyMobs = new Mob[10];
        for(int i=0,k=0;i<mobsCounter;i++){
            if(i==index){
                continue;
            }
            proxyMobs[k]=Mobs[i];
            k++;
        }
        Mobs=proxyMobs;                 // Copy New Array To Main
        mobsCounter--;*/
    }

    public boolean MobIsDead(int index){
        /*if(MobsSoftDelete.contains(index)){
            return true;
        }*/
        for(int i=0;i<deletedCounter;i++){
            if(DeletedList[i]==index){
                return true;
            }
        }

        return false;
    }

    // On Trap Check
    public boolean isTrap(int x,int y){
        if(!Traps.isEmpty()){
            for (Trap trap : Traps) {
                if(trap.getX()==x && trap.getY()==y ){
                    return true;
                }
            }
        }

        return false;
    }

    // On Door Check
    public boolean isDoor(int x, int y){
        if(Doors[0]!=null){
            for(int i=0;i<doorCounter;i++){
                if(Doors[i].getX()==x && Doors[i].getY()==y ){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isKey(int x,int y){
        if(KeyLine!=0){
            if(KeyLine==x && KeyColumm == y){
                return true;
            }
        }
        return false;
    }
    // Key Pressed Check
    public void keyPressed(){

            for (int i=0;i<doorCounter;i++){
                switch (matrix[Doors[i].getX()][Doors[i].getY()]){
                    case '=' :
                        matrix[Doors[i].getX()][Doors[i].getY()] = '_';
                        break;
                    case '_' :
                        matrix[Doors[i].getX()][Doors[i].getY()] = '=';
                        break;
                    case '"' :
                        matrix[Doors[i].getX()][Doors[i].getY()] = ')';
                        break;
                    case ')' :
                        matrix[Doors[i].getX()][Doors[i].getY()] = '"';
                        break;
                }
            }

    }



    //
    public double computeTilesOutOfPlace(MummyMazeState finalState) {
        double h = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                // Blank is ignored so that the heuristic is admissible
                if (this.matrix[i][j] != 0 && this.matrix[i][j] != finalState.matrix[i][j]) {
                    h++;
                }
            }
        }
        return h;
    }

    public double computeGoalDistance(MummyMazeState state){
        double h = 0;
        double distance = Math.abs(state.getGoalLine() -state.heroLine) +Math.abs(state.getGoalColumm() -state.heroColumn);

        return distance;
    }

    public double computeTileDistances(MummyMazeState state) {
        double h = 0;
        if(state==null){
            return h;
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                // Blank is ignored so that the heuristic is admissible
                if (this.matrix[i][j] != 0) { // Blank is ignored so that the heuristic is admissible
                   /* h += Math.abs(i - linesfinalMatrix[this.matrix[i][j]])
                            + Math.abs(j - colsfinalMatrix[this.matrix[i][j]]);*/
                }
            }
        }
        return h;
    }

    public int getNumLines() {
        return matrix.length;
    }

    public int getNumColumns() {
        return matrix[0].length;
    }

    public int getTileValue(int line, int column) {
        if (!isValidPosition(line, column)) {
            throw new IndexOutOfBoundsException("Invalid position!");
        }
        return matrix[line][column];
    }

    public boolean isValidPosition(int line, int column) {
        return line >= 0 && line < matrix.length && column >= 0 && column < matrix[0].length;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MummyMazeState)) {
            return false;
        }

        MummyMazeState o = (MummyMazeState) other;
        if (matrix.length != o.matrix.length) {
            return false;
        }

        return Arrays.deepEquals(matrix, o.matrix);
    }

    @Override
    public int hashCode() {
        return 97 * 7 + Arrays.deepHashCode(this.matrix);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < matrix.length; i++) {
            buffer.append('\n');
            for (int j = 0; j < matrix.length; j++) {
                buffer.append(matrix[i][j]);
                buffer.append(' ');
            }
        }
        return buffer.toString();
    }

    @Override
    public MummyMazeState clone() {
        return new MummyMazeState(matrix,KeyLine,KeyColumm,Traps);
    }

    private transient ArrayList<MummyMazeListener> listeners = new ArrayList<MummyMazeListener>(3);

    public synchronized void removeListener(MummyMazeListener l) {
        if (listeners != null && listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public synchronized void addListener(MummyMazeListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void firePuzzleChanged(MummyMazeEvent pe) {
        for (MummyMazeListener listener : listeners) {
            listener.puzzleChanged(null);
        }
    }

    public char[][] getMatrix() {
        return matrix;
    }

}
