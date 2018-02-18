package com.swing.mapping.component;

import com.common.bean.MappingInfo;
import com.string.widget.util.ValueWidget;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MappingRelationPane extends JPanel {
    private JCheckBox directionCheckBox;
    private JComboBox mappingComboBox;
    /***
     * 成员变量名称
     */
    private String columnName;

    /**
     * Create the panel.
     */
    public MappingRelationPane(List<MappingInfo> mappings, String columnName) {
        this.columnName = columnName;
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        directionCheckBox = new JCheckBox("双向");
        directionCheckBox.setSelected(true);
        GridBagConstraints gbc_directionhCeckBox = new GridBagConstraints();
        gbc_directionhCeckBox.insets = new Insets(0, 0, 5, 0);
        gbc_directionhCeckBox.gridx = 0;
        gbc_directionhCeckBox.gridy = 0;
        add(directionCheckBox, gbc_directionhCeckBox);

        mappingComboBox = new JComboBox();
        GridBagConstraints gbc_mappingComboBox = new GridBagConstraints();
        gbc_mappingComboBox.fill = GridBagConstraints.HORIZONTAL;
        gbc_mappingComboBox.gridx = 0;
        gbc_mappingComboBox.gridy = 1;
        add(mappingComboBox, gbc_mappingComboBox);

        init(mappings);
    }

    /***
     * 初始化下拉框<br />
     * 因为不同的情况,下拉框的选项不同
     * @param mappings
     */
    private void init(List<MappingInfo> mappings) {
        if (ValueWidget.isNullOrEmpty(mappings)) {
            return;
            /*mappings=new ArrayList<>();
            mappings.add(new MappingInfo().setDisplayName("多对一"));
            mappings.add(new MappingInfo().setDisplayName("一对一"));*/
        }
        int size = mappings.size();
        for (int i = 0; i < size; i++) {
            mappingComboBox.addItem(mappings.get(i));
        }

    }

    public boolean isTwoDirection() {
        return directionCheckBox.isSelected();
    }

    public int mappingType() {
        MappingInfo mappingInfo = (MappingInfo) this.mappingComboBox.getSelectedItem();
        if (null == mappingInfo) {
            return -1;
        }
        return mappingInfo.getType();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
