import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Jake Bashaw on 4/10/2016.
 * Oscar Onyeke
 */
public class LasersPTUI
{
    // PRIVATE VARIABLES
    private String[][] b;
    private int rsize;
    private int csize;
    private static boolean running = true;

    public static void main(String args[]) throws FileNotFoundException
    {
        if(args.length == 0)
        {
            System.out.println("Usage: java LasersPTUI safe-file [input]");
            System.exit(0);
        }
        LasersPTUI ptui = new LasersPTUI(args[0]);
        if(args.length == 2)
        {
            String filename = args[1];
            Scanner sc = new Scanner(new File(filename));
            while(sc.hasNextLine())
            {
                String command = sc.nextLine();
                System.out.println("> " + command);
                ptui.commandPicker(command);
            }
            sc.close();
        }
        Scanner kb = new Scanner(System.in);
        while(running == true)
        {
            System.out.print("> ");
            String command = kb.nextLine();
            if(command.equals(""))
            {
                command = " ";
            }
            ptui.commandPicker(command);
        }
    }

    public LasersPTUI(String filename) throws FileNotFoundException
    {
        Scanner in = new Scanner(new File(filename));
        rsize = in.nextInt();
        csize = in.nextInt();
        in.nextLine();
        b = new String[rsize][csize];
        for (int row=0; row < b.length; row++)
        {
            for (int col = 0; col < b[row].length; col++)
            {
                b[row][col] = in.next();
            }
        }
        in.close();
        System.out.println(toString());
    }

    public void commandPicker(String command)
    {
        String[] pc = command.split(" ");
        switch (command.toLowerCase().charAt(0))
        {
            case 'h':
                help();
                break;
            case 'a':
                if(pc.length == 3)
                {
                    add(Integer.parseInt(pc[1]), Integer.parseInt(pc[2]));
                }
                else
                {
                    System.out.println("Incorrect coordinates");
                }
                break;
            case 'd':
                System.out.println(toString());
                break;
            case 'r':
                if(pc.length == 3)
                {
                    remove(Integer.parseInt(pc[1]), Integer.parseInt(pc[2]));
                }
                else
                {
                    System.out.println("Incorrect coordinates");
                }
                break;
            case 'v':
                verify();
                break;
            case 'q':
                running = false;
                break;
            case ' ':
                break;
            default:
                System.out.println("Unrecognized command: " + command);
                break;
        }
    }

    public void add(int r, int c)
    {/*
    status of the add command is displayed. If the laser was successfully placed, the standard output message,
    followed by a new line, if the laser could not be placed the prints an error messages. After the status of the
    add command is displayed, the safe is redisplayed to standard output.
     */
        if(r >= rsize || r < 0 || c >= csize || c < 0)
        {
            System.out.println("Error adding laser at: (" + r + ", " + c + ")");
            System.out.println(toString());
        }
        else if(!(b[r][c].matches("[0-9]")) && !(b[r][c].equals("X")))
        {
            b[r][c] = "L";
            addLaserBeam(r, c);
            System.out.println("Laser added at: (" + r + ", " + c + ")");
            System.out.println(toString());
        }
        else
        {
            System.out.println("Error adding laser at: (" + r + ", " + c + ")");
            System.out.println(toString());
        }
    }

    public void remove(int r, int c)
    {   /*
            removes the laser from the safe, If the laser could not be removed prints an error message
            After the status of the remove command is displayed
        */
        if(r >= rsize || r < 0 || c >= csize || c < 0)
        {
            System.out.println("Error removing laser at: (" + r + ", " + c + ")");
            System.out.println(toString());
        }
        else if(b[r][c].equals("L"))
        {
            b[r][c] = ".";
            removeLaserBeam(r, c);
            for (int row=0; row < b.length; row++)
            {
                for (int col = 0; col < b[row].length; col++)
                {
                    if(b[row][col].equals("L"))
                    {
                        addLaserBeam(row, col);
                    }
                }
            }
            System.out.println("Laser removed at: (" + r + ", " + c + ")");
            System.out.println(toString());
        }
        else
        {
            System.out.println("Error removing laser at: (" + r + ", " + c + ")");
            System.out.println(toString());
        }
    }

    public void removeLaserBeam(int r, int c)
    {
        /*
        removes the laser beams from a specific starting point in four different directions
         */
        if(r > 0)
        {
            for(int row = r - 1; row >= 0; row--)
            {
                if(!b[row][c].equals("X") && !b[row][c].matches("[0-9]") && !b[row][c].equals("L"))
                {
                    b[row][c] = ".";
                }
                else
                {
                    break;
                }
            }
        }
        if(r < rsize-1)
        {
            for(int row = r + 1; row < rsize; row++)
            {
                if(!b[row][c].equals("X") && !b[row][c].matches("[0-9]") && !b[row][c].equals("L"))
                {
                    b[row][c] = ".";
                }
                else
                {
                    break;
                }
            }
        }
        if(c > 0)
        {
            for(int col = c - 1; col >= 0; col--)
            {
                if(!b[r][col].equals("X") && !b[r][col].matches("[0-9]") && !b[r][col].equals("L"))
                {
                    b[r][col] = ".";
                }
                else
                {
                    break;
                }
            }
        }
        if(c < csize-1)
        {
            for(int col = c + 1; col < csize; col++)
            {
                if(!b[r][col].equals("X") && !b[r][col].matches("[0-9]") && !b[r][col].equals("L"))
                {
                    b[r][col] = ".";
                }
                else
                {
                    break;
                }
            }
        }
    }

    public void addLaserBeam(int r, int c)
    { /*
       Adds a laser beams from a specific spot in the four cardinal directions
      */
        if(r > 0)
        {
            for(int row = r - 1; row >= 0; row--)
            {
                if(!b[row][c].equals("X") && !b[row][c].matches("[0-9]") && !b[row][c].equals("L"))
                {
                    b[row][c] = "*";
                }
                else
                {
                    break;
                }
            }
        }
        if(r < rsize-1)
        {
            for(int row = r + 1; row < rsize; row++)
            {
                if(!b[row][c].equals("X") && !b[row][c].matches("[0-9]") && !b[row][c].equals("L"))
                {
                    b[row][c] = "*";
                }
                else
                {
                    break;
                }
            }
        }
        if(c > 0)
        {
            for(int col = c - 1; col >= 0; col--)
            {
                if(!b[r][col].equals("X") && !b[r][col].matches("[0-9]") && !b[r][col].equals("L"))
                {
                    b[r][col] = "*";
                }
                else
                {
                    break;
                }
            }
        }
        if(c < csize-1)
        {
            for(int col = c + 1; col < csize; col++)
            {
                if(!b[r][col].equals("X") && !b[r][col].matches("[0-9]") && !b[r][col].equals("L"))
                {
                    b[r][col] = "*";
                }
                else
                {
                    break;
                }
            }
        }
    }

    public void verify()
    {
        /*
        displays a status message that indicates whether the safe is valid or not
         */
        String point;
        for (int row = 0; row < b.length; row++)
        {
            for (int col = 0; col < b[row].length; col++)
            {
                point = b[row][col];
                if(point.equals("."))
                {
                    System.out.println("Error verifying at: (" + row + ", " + col + ")");
                    System.out.println(toString());
                    return;
                }
                else if(point.equals("L"))
                {
                    if(!laserVer(row,col))
                    {
                        System.out.println("Error verifying at: (" + row + ", " + col + ")");
                        System.out.println(toString());
                        return;
                    }
                }
                if(point.matches("[0-9]"))
                {
                    if(!pillarVer(row, col))
                    {
                        System.out.println("Error verifying at: (" + row + ", " + col + ")");
                        System.out.println(toString());
                        return;
                    }
                }
            }
        }
        System.out.println("Safe is fully verified!");
        System.out.println(toString());
    }

    public boolean laserVer(int r, int c)
    {
        /*
        sees if a laser came in contact with another laser
         */
        String [] pillars = new String[] {"1","2","3","4","X"};
        for(int i=r;i<rsize;i++){
            if(this.b[i][c].equals("L")&&i!=r){
                return false;
            }
            else if(Arrays.asList(pillars).contains(this.b[i][c])){
                i=rsize;
            }
        }
        for(int i=r;i>=0;i--){
            if(this.b[i][c].equals("L")&&i!=r){
                return false;
            }
            else if(Arrays.asList(pillars).contains(this.b[i][c])){
                i=-1;
            }
        }
        for(int i=c;i<csize;i++){
            if(this.b[r][i].equals("L")&&i!=c){
                return false;
            }
            else if(Arrays.asList(pillars).contains(this.b[i][c])){
                i=csize;
            }
        }
        for(int i=c;i>=0;i--){
            if(this.b[r][i].equals("L")&&i!=c){
                return false;
            }
            else if(Arrays.asList(pillars).contains(this.b[i][c])){
                i=-1;
            }
        }
        return true;
    }

    public boolean pillarVer(int r, int c)
    {/*
     method that checks a pillar for if they have the specified number of lasers next to them
     */
        int count = 0;
        if(r > 0)
        {
            if(b[r-1][c].equals("L"))
            {//checks above the spot for a laser
                count++;
            }
        }
        if(r < rsize-1)
        {
            if(b[r+1][c].equals("L"))
            {
                //checks below for a laser
                count++;
            }
        }
        if(c > 0)
        {
            if(b[r][c-1].equals("L"))
            {//checks to the left for a laser
                count++;
            }
        }
        if(c < csize-1)
        {
            if(b[r][c+1].equals("L"))
            {//looks to the right for a laser
                count++;
            }
        }
        if(count == Integer.parseInt(b[r][c]))
        {//checks if the number of laser equals the number on the pillar
            return true;
        }
        return false;
    }

    @Override
    public String toString()
    {/*
     just redisplays the safe to standard output
     */
        String s = "  ";
        for (int i = 0; i < csize; i++)
        {
            if ( i >= 10)
            {
                s += i%10;
            }
            else
            {
                s += i;
            }
            s += " ";
        }
        s += "\n";
        s += "  ";
        for (int i = 0; i < (2*csize) - 1; i++)
        {
            s += "-";
        }
        s += "\n";
        for (int i = 0; i < rsize; i++)
        {
            if ( i >= 10)
            {
                s += i%10;
            }
            else
            {
                s += i;
            }
            s += "|";
            for(int j = 0; j < csize; j++)
            {
                s += b[i][j];
                if( j < csize-1)
                {
                    s+= " ";
                }
            }
            if(i < rsize-1)
            {
                s += "\n";
            }
        }
        return s;
    }

    public void help()
    {/*
         displays the help message to standard output, with no status message
     */
        System.out.println("    a|add r c: Add laser to (r,c)\n" +
                "    d|display: Display safe\n" +
                "    h|help: Print this help message\n" +
                "    q|quit: Exit program\n" +
                "    r|remove r c: Remove laser from (r,c)\n" +
                "    v|verify: Verify safe correctness");
    }
}
