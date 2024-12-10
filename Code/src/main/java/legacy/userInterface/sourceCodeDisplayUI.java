//package userInterface.UI_Panels;
//
//import userInterface.ProgramWindow;
//
//import javax.swing.*;
//import javax.swing.event.DocumentEvent;
//import javax.swing.event.DocumentListener;
//import javax.swing.undo.UndoManager;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.KeyEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.util.ArrayList;
//import java.util.Scanner;
//
//public class sourceCodeDisplayUI extends JPanel {
//    private final JList<String> fileList;
//    private final JTextArea codeArea;
//    private final DefaultListModel<String> listModel;
//    private final UndoManager undoManager;
//    private File currentDir;
//    private File currentFile;
//    private boolean hasUnsavedChanges;
//    private final JButton saveButton;
//    private final JButton undoButton;
//    private final JButton redoButton;
//    private final ProgramWindow parent;
//
//    public sourceCodeDisplayUI(ProgramWindow parent) {
//        this.parent = parent;
//        setLayout(new BorderLayout(5, 5));
//        undoManager = new UndoManager();
//
//        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JButton openButton = new JButton("Open Folder");
//        openButton.addActionListener(e -> openFolder());
//
//        saveButton = new JButton("Save");
//        saveButton.setEnabled(false);
//        saveButton.addActionListener(e -> saveFile());
//
//        undoButton = new JButton("Undo");
//        undoButton.setEnabled(false);
//        undoButton.addActionListener(e -> undo());
//
//        redoButton = new JButton("Redo");
//        redoButton.setEnabled(false);
//        redoButton.addActionListener(e -> redo());
//
//        toolBar.add(openButton);
//        toolBar.add(saveButton);
//        toolBar.add(undoButton);
//        toolBar.add(redoButton);
//
//        listModel = new DefaultListModel<>();
//        fileList = new JList<>(listModel);
//        fileList.addListSelectionListener(e -> {
//            if (!e.getValueIsAdjusting()) {
//                handleFileSelection();
//            }
//        });
//        JScrollPane listScroll = new JScrollPane(fileList);
//        listScroll.setPreferredSize(new Dimension(200, 0));
//
//        codeArea = new JTextArea();
//        codeArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
//        codeArea.getDocument().addUndoableEditListener(e -> {
//            undoManager.addEdit(e.getEdit());
//            updateButtons();
//        });
//
//        codeArea.getDocument().addDocumentListener(new DocumentListener() {
//            public void insertUpdate(DocumentEvent e) { textChanged(); }
//            public void removeUpdate(DocumentEvent e) { textChanged(); }
//            public void changedUpdate(DocumentEvent e) { textChanged(); }
//        });
//
//        addKeyboardShortcuts();
//
//        JScrollPane codeScroll = new JScrollPane(codeArea);
//        add(toolBar, BorderLayout.NORTH);
//        add(listScroll, BorderLayout.WEST);
//        add(codeScroll, BorderLayout.CENTER);
//        parent.addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                handleWindowClosing();
//            }
//        });
//    }
//
//    private void addKeyboardShortcuts() {
//        codeArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
//                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "Undo");
//        codeArea.getActionMap().put("Undo", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                undo();
//            }
//        });
//
//        codeArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
//                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "Redo");
//        codeArea.getActionMap().put("Redo", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                redo();
//            }
//        });
//
//        codeArea.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S,
//                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()), "Save");
//        codeArea.getActionMap().put("Save", new AbstractAction() {
//            public void actionPerformed(ActionEvent e) {
//                saveFile();
//            }
//        });
//    }
//
//    private void undo() {
//        if (undoManager.canUndo()) {
//            undoManager.undo();
//            updateButtons();
//        }
//    }
//
//    private void redo() {
//        if (undoManager.canRedo()) {
//            undoManager.redo();
//            updateButtons();
//        }
//    }
//
//    private void updateButtons() {
//        undoButton.setEnabled(undoManager.canUndo());
//        redoButton.setEnabled(undoManager.canRedo());
//        saveButton.setEnabled(hasUnsavedChanges);
//    }
//
//    private void textChanged() {
//        if (!hasUnsavedChanges) {
//            hasUnsavedChanges = true;
//            updateButtons();
//            updateTitle();
//        }
//    }
//
//    private void updateTitle() {
//        String fileName = currentFile != null ? currentFile.getName() : "Untitled";
//        parent.setTitle("Code Analysis Tool - " + fileName + (hasUnsavedChanges ? "*" : ""));
//    }
//
//    private void handleFileSelection() {
//        if (hasUnsavedChanges) {
//            int response = JOptionPane.showConfirmDialog(this,
//                    "Do you want to save changes to the current file?",
//                    "Unsaved Changes",
//                    JOptionPane.YES_NO_CANCEL_OPTION);
//
//            if (response == JOptionPane.YES_OPTION) {
//                saveFile();
//            } else if (response == JOptionPane.CANCEL_OPTION) {
//                return;
//            }
//        }
//        openSelectedFile();
//    }
//
//    private void handleWindowClosing() {
//        if (hasUnsavedChanges) {
//            int response = JOptionPane.showConfirmDialog(this,
//                    "Do you want to save changes before closing?",
//                    "Unsaved Changes",
//                    JOptionPane.YES_NO_CANCEL_OPTION);
//
//            if (response == JOptionPane.YES_OPTION) {
//                saveFile();
//            } else if (response == JOptionPane.CANCEL_OPTION) {
//                return;
//            }
//        }
//        parent.dispose();
//    }
//
//    private void openFolder() {
//        if (hasUnsavedChanges) {
//            int response = JOptionPane.showConfirmDialog(this,
//                    "Do you want to save changes before opening a new folder?",
//                    "Unsaved Changes",
//                    JOptionPane.YES_NO_CANCEL_OPTION);
//
//            if (response == JOptionPane.YES_OPTION) {
//                saveFile();
//            } else if (response == JOptionPane.CANCEL_OPTION) {
//                return;
//            }
//        }
//
//        JFileChooser chooser = new JFileChooser();
//        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        int result = chooser.showOpenDialog(this);
//
//        if (result == JFileChooser.APPROVE_OPTION) {
//            currentDir = chooser.getSelectedFile();
//            updateFileList();
//        }
//    }
//
//    private void updateFileList() {
//        listModel.clear();
//        try {
//            Files.list(currentDir.toPath())
//                    .filter(path -> !Files.isDirectory(path))
//                    .map(Path::getFileName)
//                    .map(Path::toString)
//                    .forEach(listModel::addElement);
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this,
//                    "Error reading directory: " + e.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void openSelectedFile() {
//        String selectedFileName = fileList.getSelectedValue();
//        if (selectedFileName == null) return;
//
//        currentFile = new File(currentDir, selectedFileName);
//        try {
//            String content = Files.readString(currentFile.toPath());
//            codeArea.setText(content);
//            codeArea.setCaretPosition(0);
//            undoManager.discardAllEdits();
//            hasUnsavedChanges = false;
//            updateButtons();
//            updateTitle();
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this,
//                    "Error reading file: " + e.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void saveFile() {
//        if (currentFile == null) {
//            JOptionPane.showMessageDialog(this,
//                    "No file is currently open",
//                    "Warning",
//                    JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        try {
//            Files.writeString(currentFile.toPath(), codeArea.getText());
//            hasUnsavedChanges = false;
//            updateButtons();
//            updateTitle();
//            JOptionPane.showMessageDialog(this,
//                    "File saved successfully",
//                    "Success",
//                    JOptionPane.INFORMATION_MESSAGE);
//        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this,
//                    "Error saving file: " + e.getMessage(),
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//        }
//    }
//}