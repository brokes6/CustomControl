package com.example.customcontrollibs;

import java.util.HashSet;
import java.util.Set;

public class SelectorGroup {
    //处于同一组的单选按钮都被保存在这个Set中
    private Set<Selector> selectors = new HashSet<>();

    public void addSelector(Selector selector) {
        selectors.add(selector);
    }

    public void setSelected(String tag) {
        for (Selector s : selectors) {
            if (s.getTag().equals(tag)) {
                s.switchSelector();
            }
        }
    }

    //当一个按钮选中时，遍历其他按钮并取消他们的选中状态
    public void setSelected(Selector selector) {
        cancelPreSelector(selector);
    }

    private void cancelPreSelector(Selector selector) {
        for (Selector s : selectors) {
            if (!s.equals(selector) && s.isSelected()) {
                s.switchSelector();
            }
        }
    }

    public Selector getSelected() {
        for (Selector s : selectors) {
            if (s.isSelected()) {
                return s;
            }
        }
        return null;
    }

    public void clear() {
        if (selectors != null) {
            selectors.clear();
        }
    }
}