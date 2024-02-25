package net.gavrix32.engine.editor;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import net.gavrix32.engine.graphics.Renderer;
import net.gavrix32.engine.graphics.Scene;
import net.gavrix32.engine.io.Window;
import net.gavrix32.engine.utils.Logger;
import net.gavrix32.engine.utils.Utils;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;

public class Editor {
    public static boolean status = true;
    private static final ImGuiImplGl3 imGuiImplGl3 = new ImGuiImplGl3();
    private static final ImGuiImplGlfw imGuiImplGlfw = new ImGuiImplGlfw();
    private static final String fontPath = "fonts/Inter-Regular.ttf";

    static {
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.getFonts().addFontFromMemoryTTF(Utils.loadBytes(fontPath), 15);
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        imGuiImplGlfw.init(Window.get(), true);
        imGuiImplGl3.init();
    }

    public static void update(ArrayList<Scene> scenes, ArrayList<String> sceneNames) {
        if (status) {
            imGuiImplGlfw.newFrame();
            ImGui.newFrame();
            ImGui.dockSpaceOverViewport();
            ImGui.beginDisabled(!Window.isCursorVisible());

            Render.update();
            Scenes.update(scenes, sceneNames);
            Logs.update();
            Viewport.update();

            ImGui.endDisabled();
            ImGui.render();
            imGuiImplGl3.renderDrawData(ImGui.getDrawData());
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupWindowPtr);
            }
        }
    }

    public static void toggle() {
        status = !status;
        Renderer.resetAccFrames();
    }
}