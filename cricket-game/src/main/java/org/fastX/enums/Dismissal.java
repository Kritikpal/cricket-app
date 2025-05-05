package org.fastX.enums;

import lombok.Data;
import org.fastX.models.Player;

@Data
public class Dismissal {
    private final DismissType dismissType;
    private final Player disMissBy;
    private final Player dismissPlayer;

    public Dismissal(DismissType dismissType, Player disMissBy, Player dismissPlayer) {
        this.dismissType = dismissType;
        this.disMissBy = disMissBy;
        this.dismissPlayer = dismissPlayer;
    }

    public String getWord() {
        StringBuilder builder = new StringBuilder();
        builder.append(dismissType.toString())
                .append(" ")
                .append(disMissBy.getFullName());
        return builder.toString();
    }

}
