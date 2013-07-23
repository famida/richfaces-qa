package org.richfaces.tests.page.fragments.impl.accordion.refactor;

public interface ComponentContainer {

    <T> T getContent(Class<T> clazz);
}