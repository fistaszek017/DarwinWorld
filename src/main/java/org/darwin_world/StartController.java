package org.darwin_world;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.darwin_world.model.map.WorldMap;
import org.darwin_world.model.map.GlobeMap;
import org.darwin_world.model.simulation.Simulation;
import org.darwin_world.model.simulation.SimulationConfig;

public class StartController {

    @FXML private TextField mapWidth;
    @FXML private TextField mapHeight;
    @FXML private TextField initialPlantCount;
    @FXML private TextField plantNutritionalValue;
    @FXML private TextField plantsToSpawnEachDay;
    @FXML private TextField initialAnimalCount;
    @FXML private TextField initialAnimalEnergyValue;
    @FXML private TextField animalDailyEnergyLoss;
    @FXML private TextField animalWellFedThreshold;
    @FXML private TextField animalReproductionCost;
    @FXML private TextField minMutationCount;
    @FXML private TextField maxMutationCount;
    @FXML private TextField genomeLength;
    @FXML private TextField delay;
    @FXML private CheckBox variantEnabled;
    @FXML private TextField cultivationEnergyThreshold;
    @FXML private TextField fertileSoilThreshold;
    @FXML private TextField fertileSoilDuration;
    @FXML private TextField largePlantCapacity;

    @FXML private VBox variantBox;

    @FXML
    private void onVariantToggle() {
        boolean enabled = variantEnabled.isSelected();
        variantBox.setVisible(enabled);
        variantBox.setManaged(enabled);
    }


    @FXML
    private void onStart() {
        int aMapWidth = Integer.parseInt(mapWidth.getText());
        int aMapHeight = Integer.parseInt(mapHeight.getText());
        int aInitialPlantCount = Integer.parseInt(initialPlantCount.getText());
        int aPlantNutritionalValue = Integer.parseInt(plantNutritionalValue.getText());
        int aPlantsToSpawnEachDay = Integer.parseInt(plantsToSpawnEachDay.getText());
        int aInitialAnimalCount = Integer.parseInt(initialAnimalCount.getText());
        int aInitialAnimalEnergyValue = Integer.parseInt(initialAnimalEnergyValue.getText());
        int aAnimalDailyEnergyLoss = Integer.parseInt(animalDailyEnergyLoss.getText());
        int aAnimalWellFedThreshold = Integer.parseInt(animalWellFedThreshold.getText());
        int aAnimalReproductionCost = Integer.parseInt(animalReproductionCost.getText());
        int aMinMutationCount = Integer.parseInt(minMutationCount.getText());
        int aMaxMutationCount = Integer.parseInt(maxMutationCount.getText());
        int aGenomeLength = Integer.parseInt(genomeLength.getText());
        int aDelay = Integer.parseInt(delay.getText());
        boolean aVariantEnabled = variantEnabled.isSelected();
        int aCultivationEnergyThreshold = Integer.parseInt(cultivationEnergyThreshold.getText());
        int aFertileSoilThreshold = Integer.parseInt(fertileSoilThreshold.getText());
        int aFertileSoilDuration = Integer.parseInt(fertileSoilDuration.getText());
        int aLargePlantCapacity = Integer.parseInt(largePlantCapacity.getText());

        SimulationConfig config = new SimulationConfig(
                aMapWidth,
                aMapHeight,
                aInitialPlantCount,
                aPlantNutritionalValue,
                aPlantsToSpawnEachDay,
                aInitialAnimalCount,
                aInitialAnimalEnergyValue,
                aAnimalDailyEnergyLoss,
                aAnimalWellFedThreshold,
                aAnimalReproductionCost,
                aMinMutationCount,
                aMaxMutationCount,
                aGenomeLength,
                aDelay,
                aVariantEnabled,
                aCultivationEnergyThreshold,
                aFertileSoilThreshold,
                aFertileSoilDuration,
                aLargePlantCapacity
        );

        WorldMap map = new GlobeMap(config);
        Simulation simulation = new Simulation(map, config, 1);

        openMainWindow(simulation, map, config);
    }

    private void openMainWindow(Simulation simulation, WorldMap map, SimulationConfig config) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Scene scene = new Scene(loader.load());

            MainController controller = loader.getController();
            int id = 1;
            controller.init(config, id);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Darwin World Simulation");
            stage.show();

            ((Stage) mapWidth.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
