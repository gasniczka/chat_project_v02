package org.jk.chat.common;


public enum Action {

    VIEW_HELP("//?", "view help info"),
    VIEW_ROOMS("//vr", "view rooms"),
    CHANGE_ROOM("//sr", "change room to roomName"),
    VIEW_FILES("//vf", "view files to download"),
    SEND_FILE("//sf", "send file fileName"),
    DOWNLOAD_FILE("//df", "download file fileName"),
    VIEW_HISTORY("//vh", "view chat history");

    private final String value;
    private final String description;


    Action(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Action getByValue(String value) {

        for (Action action : Action.values()) {
            if (action.value.equals(value)) {
                return action;
            }
        }

        return null;
    }

    public static final String HELP_MESSAGE = """
            USER HELP INFO\s
            \t //?             - view help info\s
            \t //vr            - view rooms\s
            \t //sr roomName   - change room to roomName\s
            \t //vf            - view files to download\s
            \t //sf fileName   - send file fileName\s
            \t //df fileName   - download file fileName\s
            \t //vh            - view chat history\s
            """;

}
