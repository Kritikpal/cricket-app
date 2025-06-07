package org.fastX;

import org.fastX.models.Player;

import java.util.List;

public class Players {

    // RCB Players
    public static final Player FAF_DU_PLESSIS = Player.builder().playerId(1L).fullName("F du Plessis").shortName("F du Plessis").build();
    public static final Player VIRAT_KOHLI = Player.builder().playerId(2L).fullName("V Kohli").shortName("V Kohli").build();
    public static final Player GLENN_MAXWELL = Player.builder().playerId(3L).fullName("G Maxwell").shortName("G Maxwell").build();
    public static final Player RAJAT_PATIDAR = Player.builder().playerId(4L).fullName("R Patidar").shortName("R Patidar").build();
    public static final Player DINESH_KARTHIK = Player.builder().playerId(5L).fullName("D Karthik").shortName("D Karthik").build();
    public static final Player MAHIPAL_LOMROR = Player.builder().playerId(6L).fullName("M Lomror").shortName("M Lomror").build();
    public static final Player WANINDU_HASARANGA = Player.builder().playerId(7L).fullName("W Hasaranga").shortName("W Hasaranga").build();
    public static final Player HARSHAL_PATEL = Player.builder().playerId(8L).fullName("H Patel").shortName("H Patel").build();
    public static final Player MOHAMMED_SIRAJ = Player.builder().playerId(9L).fullName("M Siraj").shortName("M Siraj").build();
    public static final Player KARN_SHARMA = Player.builder().playerId(10L).fullName("K Sharma").shortName("K Sharma").build();
    public static final Player JOSH_HAZLEWOOD = Player.builder().playerId(11L).fullName("J Hazlewood").shortName("J Hazlewood").build();

    // CSK Players
    public static final Player RUTURAJ_GAIKWAD = Player.builder().playerId(12L).fullName("R Gaikwad").shortName("R Gaikwad").build();
    public static final Player DEVON_CONWAY = Player.builder().playerId(13L).fullName("D Conway").shortName("D Conway").build();
    public static final Player SHIVAM_DUBE = Player.builder().playerId(14L).fullName("S Dube").shortName("S Dube").build();
    public static final Player MS_DHONI = Player.builder().playerId(15L).fullName("MS Dhoni").shortName("MS Dhoni").build();
    public static final Player MOEEN_ALI = Player.builder().playerId(16L).fullName("M Ali").shortName("M Ali").build();
    public static final Player RAVINDRA_JADEJA = Player.builder().playerId(17L).fullName("R Jadeja").shortName("R Jadeja").build();
    public static final Player BEN_STOKES = Player.builder().playerId(18L).fullName("B Stokes").shortName("B Stokes").build();
    public static final Player SHIVAM_SINGH = Player.builder().playerId(19L).fullName("S Singh").shortName("S Singh").build();
    public static final Player DEEPAK_CHAHAR = Player.builder().playerId(20L).fullName("D Chahar").shortName("D Chahar").build();
    public static final Player MAHEESH_THEEKSHANA = Player.builder().playerId(21L).fullName("M Theekshana").shortName("M Theekshana").build();
    public static final Player MATHEESHA_PATHIRANA = Player.builder().playerId(22L).fullName("M Pathirana").shortName("M Pathirana").build();

    public static List<Player> getRCBPlayingXI() {
        return List.of(
                FAF_DU_PLESSIS, VIRAT_KOHLI, GLENN_MAXWELL, RAJAT_PATIDAR, DINESH_KARTHIK,
                MAHIPAL_LOMROR, WANINDU_HASARANGA, HARSHAL_PATEL, MOHAMMED_SIRAJ,
                KARN_SHARMA, JOSH_HAZLEWOOD
        );
    }

    public static List<Player> getCSKPlayingXI() {
        return List.of(
                RUTURAJ_GAIKWAD, DEVON_CONWAY, SHIVAM_DUBE, MS_DHONI, MOEEN_ALI,
                RAVINDRA_JADEJA, BEN_STOKES, SHIVAM_SINGH, DEEPAK_CHAHAR,
                MAHEESH_THEEKSHANA, MATHEESHA_PATHIRANA
        );
    }
}

