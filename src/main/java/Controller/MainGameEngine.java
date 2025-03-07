package Controller;

import Model.CurrentState;
import Model.Orders;
import Model.Player;
import Utils.CommandHandler;
import View.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainGameEngine {

    MapController d_mapController=new MapController();
    PlayerController d_playerController=new PlayerController();
    CurrentState d_currentGameState = new CurrentState();

    public static void main(String[] args) {
        MainGameEngine l_mainGameEngine = new MainGameEngine();
        l_mainGameEngine.startGame();
    }

    private void startGame(){
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        commandDescription();

        while (true) {
            displayMenu();

            System.out.print("Enter your command: ");
            try {
                String l_inputCommand = l_bufferedReader.readLine();
                if (l_inputCommand.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the game. Goodbye!");
                    System.exit(0);
                } else {
                    commandHandler(l_inputCommand);
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void commandDescription() {
        System.out.println("================================== COMMAND Description ===================================");
        System.out.println("1. Initiate the Map:");
        System.out.println("   - Loads an existing map file into the game.");
        System.out.println("   - Usage: 'loadmap <your_filename(.map)>'");
        System.out.println("   - Example: 'loadmap world.map'");
        System.out.println();
        System.out.println("2. Edit the Map:");
        System.out.println("   - Opens an existing map for editing or creates a new one if the file does not exist.");
        System.out.println("   - Allows adding, modifying, or deleting continents, countries, and connections.");
        System.out.println("   - Usage: 'editmap <filename>(.map)'");
        System.out.println("   - Example: 'editmap mycustommap.map'");
        System.out.println();
        System.out.println("3. Validate the Map:");
        System.out.println("   - Checks if the map is correctly structured.");
        System.out.println("   - Ensures all countries are connected, continents are properly defined, and no isolated territories exist.");
        System.out.println("   - Usage: 'validatemap'");
        System.out.println();
        System.out.println("4. Show the Map:");
        System.out.println("   - Displays the current map in a structured text format on the command line.");
        System.out.println("   - Shows continents, countries, and their neighboring connections.");
        System.out.println("   - Usage: 'showmap'");
        System.out.println();
        System.out.println("5. Save the Map:");
        System.out.println("   - Saves the current map exactly as it was edited, preserving all changes.");
        System.out.println("   - The saved map can be reloaded later for further modifications or gameplay.");
        System.out.println("   - Usage: 'savemap <filename>'");
        System.out.println("   - Example: 'savemap editedworld.map'");
        System.out.println();
        System.out.println("6. Edit the Continent:");
        System.out.println("   - Adds or removes a continent from the map.");
        System.out.println("   - Adding a continent: 'editcontinent -add <continent_name> <control_value>'");
        System.out.println("   - Removing a continent: 'editcontinent -remove <continent_name>'");
        System.out.println("   - Example: 'editcontinent -add Europe 5'");
        System.out.println();
        System.out.println("7. Edit the Country:");
        System.out.println("   - Adds or removes a country from the map.");
        System.out.println("   - Adding a country: 'editcountry -add <country_name> <continent_name>'");
        System.out.println("   - Removing a country: 'editcountry -remove <country_name>'");
        System.out.println("   - Example: 'editcountry -add France Europe'");
        System.out.println();
        System.out.println("8. Edit the Neighbour:");
        System.out.println("   - Manages adjacency between countries.");
        System.out.println("   - Adding a connection: 'editneighbour -add <country_1> <country_2>'");
        System.out.println("   - Removing a connection: 'editneighbour -remove <country_1> <country_2>'");
        System.out.println("   - Example: 'editneighbour -add France Germany'");
        System.out.println();
        System.out.println("9. Add or Remove a Player:");
        System.out.println("   - Adds or removes a player in the game.");
        System.out.println("   - Adding: 'gameplayer -add <player_name>'");
        System.out.println("   - Removing: 'gameplayer -remove <player_name>'");
        System.out.println("   - Example: 'gameplayer -add Alex'");
        System.out.println();
        System.out.println("10. Assign Countries to Players:");
        System.out.println("   - Distributes all countries among players and assigns initial armies.");
        System.out.println("   - Must be done before starting the game.");
        System.out.println("   - Usage: 'assigncountries'");
        System.out.println();
        System.out.println("11. Exit the Game:");
        System.out.println("   - Closes the game and ends the session.");
        System.out.println("   - Usage: 'exit'");
        System.out.println();
    }

    private void displayMenu() {
        System.out.println("================================== COMMAND MENU ===================================");
        System.out.println("1. Initiate the map: (Usage: 'loadmap <your_filename(.map)>')");
        System.out.println("2. Edit the Map: (Usage: 'editmap <filename>(.map)')");
        System.out.println("3. Validate the Map: (Usage: 'validatemap')");
        System.out.println("4. Show the Map: (Usage: 'showmap')");
        System.out.println("5. Save the Map: (Usage: 'savemap <file_name_same_used_in_loadmap>')");
        System.out.println("6. Edit the Continent: (Usage: 'editcontinent -add/-remove <continent_name>')");
        System.out.println("7. Edit the Country: (Usage: 'editcountry -add/-remove <country_name>')");
        System.out.println("8. Edit the Neighbour: (Usage: 'editneighbour -add/-remove <country_id_1> <country_id_2>')");
        System.out.println("9. Add a player: (Usage: 'gameplayer -add/-remove <player_name>')");
        System.out.println("10. Assign countries and allocate armies to players: (Usage: 'assigncountries')");
        System.out.println("11. Exit the game: (Usage: 'exit')");
        System.out.println();
    }

    private void commandHandler(String p_inputCommand) throws Exception {
        CommandHandler l_commandHandler =new CommandHandler(p_inputCommand);
        String l_mainCommand = l_commandHandler.getMainCommand();
        boolean l_isMapAvailable = (d_currentGameState.getD_map() != null);

        Set<String> requiresMap = Set.of(
                "editcountry", "editcontinent", "editneighbour",
                "showmap", "gameplayer", "assigncountries",
                "validatemap", "savemap"
        );

        if (requiresMap.contains(l_mainCommand) && !l_isMapAvailable) {
            System.out.println("Error: Map not available. Use 'loadmap' or 'editmap' first.");
            return;
        }

        switch (l_mainCommand) {
            case "loadmap":
                loadMap(l_commandHandler);
                break;
            case "editmap":
                editMap(l_commandHandler);
                break;
            case "editcountry":
                editCountry(l_commandHandler);
                break;
            case "editcontinent":
                editContinent(l_commandHandler);
                break;
            case "editneighbour":
                editNeighbourCountry(l_commandHandler);
                break;
            case "showmap":
                new MapView(d_currentGameState).showMap();
                break;
            case "gameplayer":
                gamePlayer(l_commandHandler);
                break;
            case "assigncountries":
                assignCountries(l_commandHandler);
                break;
            case "validatemap":
                validateMap(l_commandHandler);
                break;
            case "savemap":
                saveMap(l_commandHandler);
                break;
            default:
                System.out.println("Invalid command. Please check the command menu and try again.");
                break;
        }
    }

    private void saveMap(CommandHandler p_commandHandler) {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);

        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            System.out.println("Save map command is not correct. Use 'savemap filename' command.");
        } else {
            for(Map<String,String> l_singleOperation : l_listOfOperations){
                if(l_singleOperation.containsKey("Arguments") && l_singleOperation.get("Arguments")!=null){
                    boolean l_isMapSaved = d_mapController.saveMap(d_currentGameState, l_singleOperation.get("Arguments"));
                    if(l_isMapSaved){
                        System.out.println("Map : "+d_currentGameState.getD_map().getD_mapName()+" saved successfully.");
                    }
                    else{
                        System.out.println("An error occured while saving the map.");
                    }
                }
                else {
                    System.out.println("Save map command is not correct. Use 'savemap filename' command.");
                }
            }
        }
    }

    private void validateMap(CommandHandler p_commandHandler) {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        if (l_listOfOperations != null && !l_listOfOperations.isEmpty()) {
            System.out.println("Validate map command is not correct. Use 'validatemap' command.");
            return;
        }

        Model.Map l_map = d_currentGameState.getD_map();
        if (l_map == null) {
            System.out.println("Map not Found!");
            return;
        }

        if (l_map.validateMap()) {
            System.out.println("Map is Valid");
        } else {
            System.out.println("Map is not Valid");
        }
    }

    private void assignCountries(CommandHandler p_commandHandler) throws IOException {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            d_playerController.assignCountry(d_currentGameState);
            d_playerController.assignArmies(d_currentGameState);
            playGame();
        }
    }

    private void playGame() throws IOException {
        if (d_currentGameState.getD_players() == null || d_currentGameState.getD_players().isEmpty()) {
            System.out.println("No players in the game.");
            return;
        }

        System.out.println("➡ Deploy armies: 'deploy <country> <num_of_armies>'");

        while (d_playerController.isUnallocatedArmiesExist(d_currentGameState)) {
            for (Player l_eachPlayer : d_currentGameState.getD_players()) {
                if (l_eachPlayer.getD_unallocatedArmies() > 0) {
                    l_eachPlayer.issueOrder();
                }
            }
        }

        while (d_playerController.isUnexecutedOrdersExist(d_currentGameState)) {
            for (Player l_eachPlayer : d_currentGameState.getD_players()) {
                Orders l_orderToExecute = l_eachPlayer.nextOrder();
                if (l_orderToExecute != null) {
                    l_orderToExecute.execute(l_eachPlayer); // Throws IOException if execution fails
                }
            }
        }

        System.out.println("All orders have been executed successfully.");
        System.out.println("Thank you for playing the game");
        System.exit(0);
    }

    private void gamePlayer(CommandHandler p_commandHandler) {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            System.out.println("Wrong command entered, Please enter the correct 'gameplayer' command.");
        }
        else {
            for (Map<String, String> l_eachMap : l_listOfOperations) {
                if (l_eachMap.containsKey("Operation") && l_eachMap.containsKey("Arguments")) {
                    d_currentGameState.addOrRemovePlayer(l_eachMap.get("Operation"), l_eachMap.get("Arguments"));
                }
            }
        }
    }

    private void editNeighbourCountry(CommandHandler p_commandHandler) throws  Exception {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if(l_listOfOperations == null || l_listOfOperations.isEmpty()){
            throw new Exception("Invalid command entered for editmap.");
        }else {
            for (Map<String ,String > l_singleOperation : l_listOfOperations){
                if(l_singleOperation.containsKey("Operation") && l_singleOperation.get("Operation")!=null && l_singleOperation.containsKey("Arguments") && l_singleOperation.get("Arguments")!=null){
                    d_mapController.editNeighbourCountry(d_currentGameState,l_singleOperation.get("Operation"),l_singleOperation.get("Arguments"));
                }
            }
        }
    }

    private void editContinent(CommandHandler p_commandHandler) throws Exception {
        List<Map<String,String>> l_listOfOperations = p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);

        if (l_listOfOperations.isEmpty()) {
            throw new Exception("Invalid Command for edit Continent");
        }

        for (Map<String, String> l_singleOperation : l_listOfOperations) {
            String l_operation = l_singleOperation.get("Operation");
            String l_arguments = l_singleOperation.get("Arguments");

            if (l_operation != null && !l_operation.isEmpty() && l_arguments != null && !l_arguments.isEmpty()) {
                d_mapController.editContinent(d_currentGameState, l_operation, l_arguments);
            } else {
                throw new Exception("Missing or invalid 'Operation' or 'Arguments' in command.");
            }
        }
    }

    private void editCountry(CommandHandler p_commandHandler) throws Exception {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations.isEmpty()) {
            throw new Exception("Invalid Command for edit Country");
        }

        for (Map<String, String> l_singleOperation : l_listOfOperations) {
            String l_operation = l_singleOperation.get("Operation");
            String l_arguments = l_singleOperation.get("Arguments");

            if (l_operation != null && !l_operation.isEmpty() && l_arguments != null && !l_arguments.isEmpty()) {
                d_mapController.editCountry(d_currentGameState, l_operation, l_arguments);
            } else {
                throw new Exception("Missing or invalid 'Operation' or 'Arguments' in command.");
            }
        }

    }




    private void editMap(CommandHandler p_commandHandler) throws Exception {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            throw new Exception("Invalid Command for edit map");
        }

        for (Map<String, String> l_singleOperation : l_listOfOperations) {
            if (!p_commandHandler.checkRequiredKey("Arguments", l_singleOperation)) {
                throw new Exception("Invalid Command for edit map operation");
            }
            d_mapController.editMap(d_currentGameState, l_singleOperation.get("Arguments"));
        }
    }

    private void loadMap(CommandHandler p_commandHandler) throws Exception{
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if(l_listOfOperations == null || l_listOfOperations.isEmpty()){
            throw new Exception("Invalid command for loadmap. Use 'loadmap file_name.map' command");
        }
        for(Map<String,String> l_singleOperation : l_listOfOperations){
            if(l_singleOperation.containsKey("Arguments")&& l_singleOperation.get("Arguments")!=null){
                Model.Map l_map =d_mapController.loadMap(d_currentGameState,l_singleOperation.get("Arguments"));
                System.out.println(l_map);
                if(l_map.validateMap()){
                    System.out.println("Map is valid.");
                }
                else{
                    System.out.println("Map is not valid.");
                }
            }
        }
    }


}
