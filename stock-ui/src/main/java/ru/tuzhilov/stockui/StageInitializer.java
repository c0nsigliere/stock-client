package ru.tuzhilov.stockui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import ru.tuzhilov.stockui.ChartApplication.StageReadyEvent;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {
    private final String applicationTitle;
    private final ApplicationContext applicationContext;

    @Value("classpath:/chart.fxml")
    private Resource chartResource;

    public StageInitializer(@Value("${spring.application.ui.title}") String applicationTitle,
                            ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent stageReadyEvent) {
        try {
            Stage stage = stageReadyEvent.getStage();

            FXMLLoader fxmlLoader = new FXMLLoader(chartResource.getURL());
            fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));

            Parent parent = fxmlLoader.load();
            stage.setScene(new Scene(parent, 800, 600));
            stage.setTitle(applicationTitle);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
