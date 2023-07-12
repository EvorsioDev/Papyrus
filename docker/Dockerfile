FROM itzg/minecraft-server:java17

COPY --chmod=755 extras/ops.json /extras/ops.json

ENV EULA=TRUE
ENV VERSION=1.17.1
ENV TYPE=PAPER
ENV SPIGET_RESOURCES=6245,28140
ENV OPS_FILE=/extras/ops.json
ENV FORCE_GAMEMODE=true
ENV MODE=creative
ENV EXEC_DIRECTLY=true

COPY --chmod=755 extras/*.jar /extras/
COPY --chmod=755 extras/bukkit-startup.yml /extras/bukkit-startup.yml
COPY --chmod=755 extras/start.sh /start.sh

VOLUME /plugin

ENTRYPOINT ["sh", "/start.sh"]