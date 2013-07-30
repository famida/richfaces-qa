package org.richfaces.tests.page.fragments.impl.contextMenu;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public final class ChoicePickerHelper {

    private ChoicePickerHelper() {
    }

    /**
     * @return Returns ChoicePicker for picking choices by visible text. The picker can have multiple conditions/filters (e.g. startsWith('xy').endsWith('z')).
     */
    public static ByVisibleTextChoicePicker byVisibleText() {
        return new ByVisibleTextChoicePicker();
    }

    /**
     * @return Returns ChoicePicker for picking choices by index.
     */
    public static ByIndexChoicePicker byIndex() {
        return new ByIndexChoicePicker();
    }

    public static class ByIndexChoicePicker implements ChoicePicker {

        private FindCommand command;

        private ByIndexChoicePicker() {
        }

        public ByIndexChoicePicker first() {
            command = new FindCommand() {

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(0);
                }

                @Override
                public String toString() {
                    return "firstIndex";
                }
            };
            return this;
        }

        public ByIndexChoicePicker index(final int index) {
            command = new FindCommand() {

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(index);
                }

                @Override
                public String toString() {
                    return "index(" + index + ")";
                }
            };
            return this;
        }

        public ByIndexChoicePicker last() {
            command = new FindCommand() {

                @Override
                public WebElement find(List<WebElement> list) {
                    return list.get(list.size() - 1);
                }

                @Override
                public String toString() {
                    return "lastIndex";
                }
            };
            return this;
        }

        @Override
        public WebElement pick(List<WebElement> options) {
            Preconditions.checkNotNull(options, "Options cannot be null.");
            Preconditions.checkArgument(!options.isEmpty(), "Options cannot be empty.");
            Preconditions.checkNotNull(command, "No filter specified.");
            return command.find(options);
        }

        @Override
        public String toString() {
            return (command == null ? "unknown index picking" : command.toString());
        }

        private interface FindCommand {

            WebElement find(List<WebElement> list);
        }
    };

    public static class ByVisibleTextChoicePicker implements ChoicePicker {

        private final List<Predicate> filters = Lists.newArrayList();

        private ByVisibleTextChoicePicker() {
        }

        private ByVisibleTextChoicePicker addFilter(Predicate<WebElement> filter) {
            filters.add(filter);
            return this;
        }

        public ByVisibleTextChoicePicker match(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().matches(str);
                }

                @Override
                public String toString() {
                    return "matches('" + str + "')";
                }
            });
        }

        @Override
        public WebElement pick(List<WebElement> options) {
            Preconditions.checkNotNull(options, "Options cannot be null.");
            Preconditions.checkArgument(!options.isEmpty(), "Options cannot be empty.");
            Preconditions.checkArgument(!filters.isEmpty(), "No filters specified.");
            Iterable<WebElement> result = Iterables.filter(options, new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    for (Predicate predicate : filters) {
                        if (!predicate.apply(input)) {
                            return FALSE;
                        }
                    }
                    return TRUE;
                }
            });

            return (result.iterator().hasNext() ? result.iterator().next() : null);
        }

        public ByVisibleTextChoicePicker startsWith(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().startsWith(str);
                }

                @Override
                public String toString() {
                    return "startsWith('" + str + "')";
                }
            });
        }

        public ByVisibleTextChoicePicker contains(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().contains(str);
                }

                @Override
                public String toString() {
                    return "contains('" + str + "')";
                }
            });
        }

        public ByVisibleTextChoicePicker endsWith(final String str) {
            return addFilter(new Predicate<WebElement>() {

                @Override
                public boolean apply(WebElement input) {
                    return input.getText().endsWith(str);
                }

                @Override
                public String toString() {
                    return "endsWith('" + str + "')";
                }
            });
        }

        @Override
        public String toString() {
            return (filters.isEmpty() ? "no filters specified" : filters.toString());
        }
    };
}