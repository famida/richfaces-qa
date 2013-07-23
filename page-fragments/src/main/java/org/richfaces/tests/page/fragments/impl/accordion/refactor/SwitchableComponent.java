package org.richfaces.tests.page.fragments.impl.accordion.refactor;

public interface SwitchableComponent {

    ComponentContainer switchTo(Picker picker);

    ComponentContainer switchTo(String header);

    ComponentContainer switchTo(int index);
}
