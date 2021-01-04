package com.jetbrains;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        personList people = new personlist();
        fileMngt fm = new fileMngt("data.csv", people);
        WinMngt wm = new WinMngt(people, fm);

        wm.mainMenu();
    }

    private static class personlist extends personList {
    }
}
class nameAge
{
    String name;
    int age;

    public nameAge(String name, int age)
    {
        this.name = name;
        this.age = age;
    }
}

class personList
{
    public ArrayList<nameAge> nameAgeArrayList = new ArrayList<>();

    public void addEntry(String name, int age)     //Add Entry

    {
        nameAge newPrsn = new nameAge(name, age);
        nameAgeArrayList.add(newPrsn);
    }

    public void deleteEntry(int index)       //Delete Entry
    {
        nameAgeArrayList.remove(index);
    }

    public void updateEntry(String name, int age, int index)      //Update An Entry
    {
        nameAgeArrayList.get(index).name = name;
        nameAgeArrayList.get(index).age = age;
    }
}

final class fileMngt
{
    String filePath = "data.txt";
    personList people;

    public fileMngt (String filePath, personList people)
    {
        this.people = people;
        this.filePath = filePath;
        CreateFile();
        ReadFile();
    }

    public void CreateFile()
    {
        try
        {
            File myFile = new File(filePath);
            if (myFile.createNewFile())
            {
                System.out.println("File created: " + myFile.getName());
            } else
            {
                System.out.println("File already exists.");
            }
        } catch (IOException e)
        {
            System.out.println("An error occured.");
        }
    }

    public void ReadFile ()
    {
        try
        {
            File myFile = new File(filePath);
            try (Scanner myReader = new Scanner(myFile) //Converts Text File into PersonArrayList Format
            ) {
                while (myReader.hasNextLine())
                {
                    String line = myReader.nextLine();
                    String[] lineArray = line.split(",");
                    nameAge newPrsn = new nameAge(lineArray[0], Integer.parseInt(lineArray[1]));
                    people.nameAgeArrayList.add(newPrsn);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An Error Occurred.");
        }
    }

    public void WriteToFile()
    {
        try
        {
            try (FileWriter myWriter = new FileWriter(filePath)) {
                for (nameAge p : people.nameAgeArrayList)
                {
                    myWriter.write(p.name+","+p.age+"\n");
                }
            }
            System.out.println("Success in Putting Changes in a File.\n \n");
        } catch (IOException e)
        {
            System.out.println("An Error Occurred.");
        }
    }
}

class WinMngt {
    personList people;
    fileMngt fm;

    public WinMngt(personList people, fileMngt fm) {
        this.people = people;
        this.fm = fm;
    }

    public void mainMenu() {
        int option = 0;
        String input = JOptionPane.showInputDialog("Select option \n \n" +
                "1. Add Entry \n" +
                " 2. Delete Entry \n" +
                " 3. View all entries \n" +
                " 4. Update An Entry\n" +
                " 0. Exit");
         option = Integer.parseInt(input);
        do {

            switch (option) {
                case 1:
                    addEntry();
                    break;
                case 2:
                    delEntry();
                    break;
                case 3:
                    viewEntry();
                    break;
                case 4:
                    updateEntry();
                    break;
                case 0:
                    System.exit(0);
                    break;

            }

        } while(true);


    }
    public void addEntry(){

            JTextField name = new JTextField();
            JTextField age = new JTextField();
            Object[] addField =
                    {
                            "               ADD ENTRY " +
                                    "                      \n\n",
                            "                    Name: ", name,
                            "                      Age: ", age};

            int confirm = JOptionPane.showConfirmDialog(null, addField, "Add Entry", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                try {
                    int ageInt = Integer.parseInt(age.getText());
                    people.addEntry(name.getText(), ageInt);
                    fm.WriteToFile();
                    String message = "You have Successfully Added " + name.getText();
                    showSuccess(message);
                } catch (NumberFormatException e) {
                        addEntry();
                }
            } else {
                mainMenu();
            }

        }
    public void delEntry(){

            JTextField deleteIndex = new JTextField();
            String entriesField = "";
            for (int i = 0; i < people.nameAgeArrayList.size(); i++) {
                entriesField += i + 1 + ". " + people.nameAgeArrayList.get(i).name + " is " + people.nameAgeArrayList.get(i).age + " years old." + "\n";
            }
            entriesField += "\n";

            Object[] deleteField =
                    {
                            entriesField,
                            "              DELETE ENTRY " +
                                    "                      \n",
                            "Please Select Number to Delete: ",
                            deleteIndex
                    };

            int confirm = JOptionPane.showConfirmDialog(null, deleteField, "Delete Entry", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                try {
                    int deleteIndexInt = Integer.parseInt(deleteIndex.getText()) - 1;
                    if (deleteIndexInt >= people.nameAgeArrayList.size()) {
                        throw new IndexOutOfBoundsException("Index " + deleteIndexInt + " is out of bounds!");
                    }
                    String message = "You have Successfully Deleted " + people.nameAgeArrayList.get(deleteIndexInt).name + ". ";
                    people.deleteEntry(deleteIndexInt);
                    fm.WriteToFile();

                    showSuccess(message);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        delEntry();
                }
            } else {
                mainMenu();
            }
        }
    public void viewEntry(){

            String[] entriesButton = {"Back To Menu"};
            String entriesField = "";

            for (int i = 0; i < people.nameAgeArrayList.size(); i++) {
                entriesField += i + 1 + ". " + people.nameAgeArrayList.get(i).name + " is " + people.nameAgeArrayList.get(i).age + " years old.\n";
            }
            entriesField += "\n";
            JOptionPane.showOptionDialog(null, entriesField, "View All Entries", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, entriesButton, entriesButton[0]);
            mainMenu();
        }
    public void updateEntry(){

            JTextField updateIndex = new JTextField();
            String entriesField = "";
            for (int i = 0; i < people.nameAgeArrayList.size(); i++) {
                entriesField += i + 1 + ". " + people.nameAgeArrayList.get(i).name + " is " + people.nameAgeArrayList.get(i).age + " years old." + "\n";
            }
            entriesField += "\n";

            Object[] updateField =
                    {
                            entriesField,
                            "               UPDATE ENTRY " +
                                    "                      \n",
                            "Select a Number to Update: ",
                            updateIndex
                    };
            int confirm = JOptionPane.showConfirmDialog(null, updateField, "Update An Entry", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                try {
                    int updateIndexInt = Integer.parseInt(updateIndex.getText()) - 1;
                    if (updateIndexInt >= people.nameAgeArrayList.size()) {
                        throw new IndexOutOfBoundsException("Index " + updateIndexInt + " is out of bounds!");
                    }
                    showUpdatePrompt(updateIndexInt);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    updateEntry();
                }
            } else {
                mainMenu();
            }
        }

    public void showUpdatePrompt(int updateIndexInt)
    {
        JTextField newName = new JTextField();
        JTextField newAge = new JTextField();
        Object[] updateField =
                {
                        "Current Name: " + people.nameAgeArrayList.get(updateIndexInt).name,
                        "Current Age: " + people.nameAgeArrayList.get(updateIndexInt).age + "\n\n",
                        "                     New Name: ", newName,
                        "                     New Age: ", newAge,
                        "\n"
                };
        int confirm = JOptionPane.showConfirmDialog(null, updateField, "Updating " + people.nameAgeArrayList.get(updateIndexInt).name, JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION)
        {
            try
            {
                int newAgeInt = Integer.parseInt(newAge.getText());
                String message = people.nameAgeArrayList.get(updateIndexInt).name +
                        " is Successfully Updated to\n" + newName.getText() + " with the Age of " + newAge.getText() + ". " ;
                people.updateEntry(newName.getText(), newAgeInt, updateIndexInt);
                fm.WriteToFile();
                showSuccess(message);
            }
            catch (NumberFormatException e)
            {
                showUpdatePrompt(updateIndexInt);
            }
        }

    }

    public void showSuccess(String message) {
        {
            String[] successField = {"Back To Menu"};
            JOptionPane.showOptionDialog(null, message, "Success!!", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, successField, successField[0]);
            mainMenu();
        }
    }

}