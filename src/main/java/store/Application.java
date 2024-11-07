package store;

import store.controller.ConvenienceController;
import store.view.InputView;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
        // TODO: 프로그램 구현

        ConvenienceController convenienceController = new ConvenienceController();
        convenienceController.run();
    }
}
