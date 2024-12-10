package com.github.tesquo.expplugin.listeners;

import com.github.tesquo.expplugin.EXPCode.XPEvaluator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RunPlugin extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {return;}
        Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (editor != null) {
            VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
            if (file != null) {
                try {
                    String content = new String(file.contentsToByteArray(), StandardCharsets.UTF_8);
                    XPEvaluator Evaluate = new XPEvaluator(content);
                    int score = Evaluate.normalisedScore();
                    System.out.println("Averaged Score: " + score);
                } catch (Exception ex) {ex.printStackTrace();}
            } else {System.out.println("No file opened");}
        }
    }

}

