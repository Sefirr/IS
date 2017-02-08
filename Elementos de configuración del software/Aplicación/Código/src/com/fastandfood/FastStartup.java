package com.fastandfood;

import com.fastandfood.core.FastCore;
import com.fastandfood.dao.Database;
import com.fastandfood.gui.FrameController;
import com.fastandfood.gui.MainFrame;
import com.fastandfood.gui.Material;
import com.fastandfood.gui.StartupFrame;

/**
 * Punto de entrada del programa.
 * Carga los archivos en la aplicación y presenta la interfaz al usuario.
 *
 * @author Borja
 * @see com.fastandfood.core.FastCore
 */
public class FastStartup {

    public static void main(String... args) {

        Material.setLookAndFeel();

        Database database           = new Database();
        FastCore core               = new FastCore(database);
        FrameController controller  = new FrameController(core);

        StartupFrame startupFrame   = new StartupFrame(controller);
        MainFrame mainFrame         = new MainFrame(controller);

        controller.addViews(startupFrame, mainFrame);
        startupFrame.enable();
    }
}
