package ru.tuzhilov.stockui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import org.springframework.stereotype.Component;
import ru.tuzhilov.stockclient.StockClient;
import ru.tuzhilov.stockclient.StockPrice;

import java.util.function.Consumer;

import static java.lang.String.valueOf;
import static javafx.collections.FXCollections.observableArrayList;

@Component
public class ChartController {
    @FXML
    public LineChart<String, Double> chart;

    private StockClient stockClient;

    public ChartController(StockClient client) {
        stockClient = client;
    }

    @FXML
    public void initialize() {
        PriceSubscriber priceSubscriber1 = new PriceSubscriber("SYMBOL1", stockClient);
        PriceSubscriber priceSubscriber2 = new PriceSubscriber("SYMBOL2", stockClient);

        ObservableList<Series<String, Double>> data = observableArrayList();
        data.add(priceSubscriber1.getSeries());
        data.add(priceSubscriber2.getSeries());
        chart.setData(data);
    }


    private class PriceSubscriber implements Consumer<StockPrice> {
        public Series<String, Double> getSeries() {
            return series;
        }

        private Series<String, Double> series;
        private ObservableList<Data<String, Double>> seriesData = observableArrayList();

        public PriceSubscriber(String symbol, StockClient stockClient) {
            this.series = new Series<>(symbol, seriesData);
            stockClient.pricesFor(symbol).subscribe(this);
        }

        @Override
        public void accept(StockPrice stockPrice) {
            Platform.runLater(() ->
                    seriesData.add(new Data<>(valueOf(stockPrice.getTime().getSecond()), stockPrice.getPrice()))
            );
        }
    }
}