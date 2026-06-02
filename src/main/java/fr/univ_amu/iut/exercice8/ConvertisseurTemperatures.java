package fr.univ_amu.iut.exercice8;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

/**
 * Exercice 8 (capstone) - Convertisseur de températures.
 *
 * <p>Cet exercice synthétise tous les types de bindings vus dans le TP :
 *
 * <ul>
 *   <li>Binding unidirectionnel (Labels de lecture)
 *   <li>Binding bidirectionnel (TextField ↔ Slider via {@link NumberStringConverter})
 *   <li>{@code ChangeListener} pour la conversion avec formule (C = (F-32)*5/9)
 *   <li>Sliders verticaux ({@code Orientation.VERTICAL})
 * </ul>
 *
 * <p>L'application affiche deux panneaux côte à côte : un pour Celsius, un pour Fahrenheit.
 * Modifier l'un met à jour l'autre automatiquement.
 */
public class ConvertisseurTemperatures extends Application {

  private boolean updating = false;

  @Override
  public void start(Stage primaryStage) {
    // TODO exercice 8 : construire le convertisseur de températures.
    //
    // 1. Créer le panneau Celsius (VBox) :
    // - Label "°C" (style bold, 16px)
    // - Slider vertical [0, 100], valeur initiale 0, id "slider-celsius"
    // - TextField, id "tf-celsius", maxWidth 50
    //
    // 2. Créer le panneau Fahrenheit (VBox) :
    // - Label "°F" (style bold, 16px)
    // - Slider vertical [0, 212], valeur initiale 32, id "slider-fahrenheit"
    // - TextField, id "tf-fahrenheit", maxWidth 50
    //
    // 3. Ajouter un ChangeListener sur le slider Celsius :
    // fahrenheit = celsius * 9/5 + 32
    // (utiliser un flag "updating" pour éviter les boucles infinies)
    //
    // 4. Ajouter un ChangeListener sur le slider Fahrenheit :
    // celsius = (fahrenheit - 32) * 5/9
    //
    // 5. Lier chaque TextField à son slider via
    // Bindings.bindBidirectional(tf.textProperty(), slider.valueProperty(),
    // new NumberStringConverter())
    //
    // 6. Composer les panneaux dans un HBox, créer la Scene, afficher.

    Label labelC = new Label("°C");
    labelC.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

    Slider sliderC = new Slider(0, 100, 0);
    sliderC.setOrientation(javafx.geometry.Orientation.VERTICAL);
    sliderC.setId("slider-celsius");

    TextField tfC = new TextField();
    tfC.setId("tf-celsius");
    tfC.setMaxWidth(50);

    VBox paneC = new VBox(10, labelC, sliderC, tfC);

    Label labelF = new Label("°F");
    labelF.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

    Slider sliderF = new Slider(0, 212, 32);
    sliderF.setOrientation(javafx.geometry.Orientation.VERTICAL);
    sliderF.setId("slider-fahrenheit");

    TextField tfF = new TextField();
    tfF.setId("tf-fahrenheit");
    tfF.setMaxWidth(50);

    VBox paneF = new VBox(10, labelF, sliderF, tfF);

    Bindings.bindBidirectional(
        tfC.textProperty(), sliderC.valueProperty(), new NumberStringConverter());
    Bindings.bindBidirectional(
        tfF.textProperty(), sliderF.valueProperty(), new NumberStringConverter());

    ChangeListener<Number> cToF =
        (obs, oldVal, newVal) -> {
          if (updating) return;

          updating = true;
          double c = newVal.doubleValue();
          double f = c * 9 / 5 + 32;
          sliderF.setValue(f);
          updating = false;
        };

    ChangeListener<Number> fToC =
        (obs, oldVal, newVal) -> {
          if (updating) return;

          updating = true;
          double f = newVal.doubleValue();
          double c = (f - 32) * 5 / 9;
          sliderC.setValue(c);
          updating = false;
        };

    sliderC.valueProperty().addListener(cToF);
    sliderF.valueProperty().addListener(fToC);

    HBox root = new HBox(30, paneC, paneF);

    Scene scene = new Scene(root, 300, 300);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Convertisseur de températures");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
