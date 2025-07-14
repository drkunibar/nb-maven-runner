package io.github.netbeans.mvnrunner.action;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.netbeans.api.annotations.common.StaticResource;
import org.openide.util.ImageUtilities;

import lombok.Getter;

import io.github.netbeans.mvnrunner.favorite.FavoriteDescriptor;
import io.github.netbeans.mvnrunner.util.ImageUtils;

public class FavoriteDlg extends JDialog {

    private static final @StaticResource String FAVORITES_ICON = "io/github/netbeans/mvnrunner/resources/favorites.png"; // NOI18N

    private final JTextField nameField;
    private final JTextField imageField;
    private final JButton imageBtn;
    private final JTextArea descriptionField;
    @Getter
    private boolean okClicked;

    public FavoriteDlg(Frame owner, FavoriteDescriptor descriptor) {
        super(owner, Dialog.ModalityType.APPLICATION_MODAL);
        this.okClicked = false;
        this.nameField = new JTextField(descriptor.getName());
        this.nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        this.imageField = new JTextField(descriptor.getImageUri() == null ? "" : descriptor.getImageUri());
        this.imageBtn = new JButton();
        this.descriptionField = new JTextArea(descriptor.getDescription());
        imageField.setEditable(false);
        imageField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        initComponents();
    }

    private void initComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        contentPane.add(createLabelPanel("Name"));
        contentPane.add(nameField);
        contentPane.add(createLabelPanel("Icon"));
        contentPane.add(createIconPanel());
        contentPane.add(createLabelPanel("Description"));
        contentPane.add(descriptionField);
        contentPane.add(createButtonPanel());
    }

    private JPanel createLabelPanel(String label) {
        Border border = BorderFactory.createEmptyBorder(2, 0, 2, 0);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(border);
        JLabel jLabel = new JLabel(label);
        panel.add(jLabel);
        panel.add(new JSeparator());
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPane = new JPanel();
        // OK button
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
        JButton okButton = new JButton("Save");
        okButton.addActionListener(this::onOk);
        buttonPane.add(okButton);
        // ---
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(5, 5));
        buttonPane.add(separator);
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this::onCancel);
        buttonPane.add(cancelButton);
        return buttonPane;
    }

    private JPanel createIconPanel() {
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
        // Button with the selected Image
        ImageIcon defaultImage = ImageUtilities.loadImageIcon(FAVORITES_ICON, true);
        Image image = ImageUtils.getImage(getImageUri(), ImageUtilities.icon2Image(defaultImage));
        imageBtn.setIcon(ImageUtilities.image2Icon(ImageUtils.resize(image, 16, 16)));
        iconPanel.add(imageBtn);
        // Select button
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            selectImage();
        });
        iconPanel.add(selectButton);
        // Url field
        iconPanel.add(imageField);
        return iconPanel;
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(
                new FileNameExtensionFilter("Image Files (.png, .jpg, .jpeg)", "png", "jpg", "jpeg"));
        int btnValue = fileChooser.showOpenDialog(this);
        if (btnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            this.imageField.setText(selectedFile.getAbsolutePath());
            ImageIcon defaultImage = ImageUtilities.loadImageIcon(FAVORITES_ICON, true);
            Image image = ImageUtils.getImage(getImageUri(), ImageUtilities.icon2Image(defaultImage));
            this.imageBtn.setIcon(new ImageIcon(image));
        }
    }

    private void onOk(ActionEvent e) {
        okClicked = true;
        this.hide();
    }

    private void onCancel(ActionEvent e) {
        okClicked = false;
        this.hide();
    }

    @Override
    public String getName() {
        return nameField.getText();
    }

    public String getDescription() {
        return descriptionField.getText();
    }

    public String getImageUri() {
        return imageField.getText();
    }

    @Override
    public void setVisible(boolean b) {
        if (b) {
            okClicked = false;
        }
        super.setVisible(b);
    }
}
