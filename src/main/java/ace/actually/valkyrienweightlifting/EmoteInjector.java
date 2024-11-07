package ace.actually.valkyrienweightlifting;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EmoteInjector {

    public static final List<String> EMOTE = List.of("""
            {
              "version": 1,
              "valid": true,
              "name": "Lift",
              "description": "Lift those weights! (based on Hands up)",
              "author": "dsty (edited by Acrogenous)",
              "emote": {
                "beginTick": 0,
                "endTick": 60,
                "stopTick": 61,
                "isLoop": true,
                "returnTick": 5,
                "nsfw": false,
                "degrees": false,
                "moves": [
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "head": {
                      "x": -0.0
                    }
                  },
                  {
                    "tick": 55,
                    "easing": "INOUTQUAD",
                    "head": {
                      "x": -0.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "head": {
                      "y": 3.0
                    }
                  },
                  {
                    "tick": 55,
                    "easing": "INOUTQUAD",
                    "head": {
                      "y": 3.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "head": {
                      "z": -0.0
                    }
                  },
                  {
                    "tick": 55,
                    "easing": "INOUTQUAD",
                    "head": {
                      "z": -0.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "head": {
                      "pitch": 0.21476103
                    }
                  },
                  {
                    "tick": 15,
                    "easing": "INOUTQUAD",
                    "head": {
                      "pitch": 0.1526575
                    }
                  },
                  {
                    "tick": 25,
                    "easing": "INOUTQUAD",
                    "head": {
                      "pitch": 0.19305594
                    }
                  },
                  {
                    "tick": 35,
                    "easing": "INOUTQUAD",
                    "head": {
                      "pitch": 0.11880231
                    }
                  },
                  {
                    "tick": 45,
                    "easing": "INOUTQUAD",
                    "head": {
                      "pitch": 0.34507892
                    }
                  },
                  {
                    "tick": 55,
                    "easing": "INOUTQUAD",
                    "head": {
                      "pitch": 0.21476103
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "head": {
                      "yaw": -0.0
                    }
                  },
                  {
                    "tick": 15,
                    "easing": "INOUTQUAD",
                    "head": {
                      "yaw": 0.048418973
                    }
                  },
                  {
                    "tick": 25,
                    "easing": "INOUTQUAD",
                    "head": {
                      "yaw": -0.19085625
                    }
                  },
                  {
                    "tick": 35,
                    "easing": "INOUTQUAD",
                    "head": {
                      "yaw": -0.12003175
                    }
                  },
                  {
                    "tick": 45,
                    "easing": "INOUTQUAD",
                    "head": {
                      "yaw": -0.039886698
                    }
                  },
                  {
                    "tick": 55,
                    "easing": "INOUTQUAD",
                    "head": {
                      "yaw": -0.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "head": {
                      "roll": 0.0
                    }
                  },
                  {
                    "tick": 15,
                    "easing": "INOUTQUAD",
                    "head": {
                      "roll": -0.018880064
                    }
                  },
                  {
                    "tick": 25,
                    "easing": "INOUTQUAD",
                    "head": {
                      "roll": 0.021056637
                    }
                  },
                  {
                    "tick": 35,
                    "easing": "INOUTQUAD",
                    "head": {
                      "roll": -0.00216202
                    }
                  },
                  {
                    "tick": 45,
                    "easing": "INOUTQUAD",
                    "head": {
                      "roll": 0.12632039
                    }
                  },
                  {
                    "tick": 55,
                    "easing": "INOUTQUAD",
                    "head": {
                      "roll": 0.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "rightArm": {
                      "x": -5.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "rightArm": {
                      "y": 2.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "rightArm": {
                      "z": -0.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "rightArm": {
                      "pitch": -2.9447277
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "rightArm": {
                      "yaw": -0.1277915
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "rightArm": {
                      "roll": -0.14755993
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "leftArm": {
                      "x": 5.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "leftArm": {
                      "y": 2.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "leftArm": {
                      "z": -0.0
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "leftArm": {
                      "pitch": -3.1465175
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "leftArm": {
                      "yaw": 0.15647896
                    }
                  },
                  {
                    "tick": 5,
                    "easing": "INOUTQUAD",
                    "leftArm": {
                      "roll": 0.12851001
                    }
                  }
                ]
              }
            }""");

    public static void injectEmote()
    {
        try {
            FileUtils.writeLines(new File("emotes/lift.json"),EMOTE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
