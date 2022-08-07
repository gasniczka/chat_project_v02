package org.jk.chat.common;


public enum Action {

    VIEW_HELP("//?", "view help info"),
    VIEW_ROOMS("//vr", "view rooms"),
    CHANGE_ROOM("//sr", "change room to roomName"),
    VIEW_FILES("//vf", "view files to download"),
    SEND_FILE("//sf", "send file fileName"),
    DOWNLOAD_FILE("//df", "download file fileName"),
    VIEW_HISTORY("//vh", "view chat history"),
    SEND_FILE_REST("//sfrest", "send file fileName by rest"),
    DOWNLOAD_FILE_REST("//dfrest", "download file fileName by rest")
    ;


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
            \t //?                 - view help info\s
            \t //vr                - view rooms\s
            \t //sr roomName       - change room to roomName\s
            \t //vf                - view files to download\s
            \t //sf fileName       - send file fileName\s
            \t //df fileName       - download file fileName\s
            \t //vh                - view chat history\s
            \t //sfrest fileName   - send file fileName using rest\s
            \t //dfrest fileName   - download file fileName using rest\s
            """;

}
