package pro.sky.skyplagin;


import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.project.Project;


import javax.lang.model.element.Element;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PanelContent extends DialogWrapper {
    private JTextField value1TextField;
    private JTextField value2TextField;
    private JButton getResultButton;
    private JButton getSocketButton;
    private JButton getTreeButton;
    //private JTextArea resultTextArea;
    private JPanel SkyPanel;
    private JFormattedTextField formattedTextField1;
    private JTextArea textArea1;

    //private JFormattedTextField formattedTextField2;

    public PanelContent(@Nullable Project project){
        super(true);
        init();
        getResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Object result = Integer.parseInt(value1TextField.getText()) + Integer.parseInt(value2TextField.getText());
                    formattedTextField1.setValue(result);
                } catch (NumberFormatException ex) {
                    formattedTextField1.setValue("value1 and value2 they should be numbers!");
                }
            }
        });

        getTreeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String projectName = project.getName();
                File file = new File(project.getBasePath());
                StringBuilder sb = new StringBuilder();
                sb.append(file.getName() + "\n");
                int count = 0;
                doFilePath(count, sb , file.listFiles());
                textArea1.setText(sb.toString());
            }
        });
    }
    private void doFilePath(int count , StringBuilder sb, File[] files){
        count++;
        for (File file : files) {
            for(int i = 0; i < count ;i++){
                sb.append("---");
            }
            sb.append(file.getName() + "\n");
            if (file.isDirectory()){
                doFilePath(count,sb,file.listFiles());
            }
        }
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return SkyPanel;
    }
}
