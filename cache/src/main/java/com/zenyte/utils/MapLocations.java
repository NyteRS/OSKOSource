package com.zenyte.utils;

import com.zenyte.utils.efficientarea.Area;
import com.zenyte.utils.efficientarea.Polygon;
import com.zenyte.utils.efficientarea.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Woox
 * @author Leanbow
 * @author Jire
 */
public final class MapLocations {

    public static final int MAX_Z = 4;

    @SuppressWarnings("unchecked")
    private static final List<Shape>[] MULTICOMBAT = new List[MAX_Z];
    private static final Area[] MULTICOMBAT_CACHE = new Area[MAX_Z];

    @SuppressWarnings("unchecked")
    private static final List<Shape>[] NOT_MULTICOMBAT = new List[MAX_Z];

    @SuppressWarnings("unchecked")
    private static final List<Shape>[] ROUGH_WILDERNESS = new List[MAX_Z];
    private static final Area[] ROUGH_WILDERNESS_CACHE = new Area[MAX_Z];

    @SuppressWarnings("unchecked")
    private static final List<Shape>[] WILDERNESS_LEVEL_LINES = new List[MAX_Z];
    private static final Area[] WILDERNESS_LEVEL_LINES_CACHE = new Area[MAX_Z];

    @SuppressWarnings("unchecked")
    private static final List<Shape>[] DEADMAN_SAFE_ZONES = new List[MAX_Z];
    private static final Area[] DEADMAN_SAFE_ZONES_CACHE = new Area[MAX_Z];

    @SuppressWarnings("unchecked")
    private static final List<Shape>[] PVP_WORLD_SAFE_ZONES = new List[MAX_Z];
    private static final Area[] PVP_SAFE_ZONES_CACHE = new Area[MAX_Z];

    @SuppressWarnings("unchecked")
    private static final List<Shape>[] SINGLES_PLUS_LIST = new List[MAX_Z];
    private static final Area[] SINGLES_PLUS_CACHE = new Area[MAX_Z];

    private static Area getArea(int plane, List<Shape> shapes) {
        Area area = new Area();
        for (Shape shape : shapes) {
            area.add(new Area(shape));
        }
        return area;
    }

    private static Area getArea(List<Shape> shapes, Rectangle view) {
        Area area = new Area();
        for (Shape shape : shapes) {
            if (shape.intersects(view)) {
                area.add(new Area(shape));
            }
        }
        return area;
    }

    public static Area getMulticombat(int plane) {
        if (MULTICOMBAT_CACHE[plane] == null) {
            Area area = getArea(plane, MULTICOMBAT[plane]);
            area.subtract(getArea(plane, NOT_MULTICOMBAT[plane]));
            MULTICOMBAT_CACHE[plane] = area;
        }
        return MULTICOMBAT_CACHE[plane];
    }

    public static Area getMulticombat(Rectangle view, int plane) {
        Area area = getArea(MULTICOMBAT[plane], view);
        area.subtract(getArea(NOT_MULTICOMBAT[plane], view));
        return area;
    }

    public static Area getRoughWilderness(int plane) {
        if (ROUGH_WILDERNESS_CACHE[plane] == null) {
            ROUGH_WILDERNESS_CACHE[plane] = getArea(plane, ROUGH_WILDERNESS[plane]);
        }
        return ROUGH_WILDERNESS_CACHE[plane];
    }

    public static Area getRoughWilderness(Rectangle view, int plane) {
        return getArea(ROUGH_WILDERNESS[plane], view);
    }

    public static Area getWildernessLevelLines(int plane) {
        if (WILDERNESS_LEVEL_LINES_CACHE[plane] == null) {
            WILDERNESS_LEVEL_LINES_CACHE[plane] = getArea(plane, WILDERNESS_LEVEL_LINES[plane]);
        }
        return WILDERNESS_LEVEL_LINES_CACHE[plane];
    }

    public static Area getWildernessLevelLines(Rectangle view, int plane) {
        return getArea(WILDERNESS_LEVEL_LINES[plane], view);
    }

    public static Area getDeadmanSafeZones(int plane) {
        if (DEADMAN_SAFE_ZONES_CACHE[plane] == null) {
            DEADMAN_SAFE_ZONES_CACHE[plane] = getArea(plane, DEADMAN_SAFE_ZONES[plane]);
        }
        return DEADMAN_SAFE_ZONES_CACHE[plane];
    }

    public static Area getDeadmanSafeZones(Rectangle view, int plane) {
        return getArea(DEADMAN_SAFE_ZONES[plane], view);
    }

    public static Area getPvpSafeZones(int plane) {
        if (PVP_SAFE_ZONES_CACHE[plane] == null) {
            PVP_SAFE_ZONES_CACHE[plane] = getArea(plane, PVP_WORLD_SAFE_ZONES[plane]);
        }
        return PVP_SAFE_ZONES_CACHE[plane];
    }

    public static Area getPvpSafeZones(Rectangle view, int plane) {
        return getArea(PVP_WORLD_SAFE_ZONES[plane], view);
    }

    public static Area getSinglesPlusZones(int plane) {
        if (SINGLES_PLUS_CACHE[plane] == null) {
            SINGLES_PLUS_CACHE[plane] = getArea(plane, SINGLES_PLUS_LIST[plane]);
        }
        return SINGLES_PLUS_CACHE[plane];
    }

    public static Area getSinglesPlusZones(Rectangle view, int plane) {
        return getArea(SINGLES_PLUS_LIST[plane], view);
    }

    private static void initializeWithEmptyLists(List<Shape>[] array) {
        for (int plane = 0; plane < array.length; plane++) {
            array[plane] = new ArrayList<>();
        }
    }

    static {
        initializeWithEmptyLists(MULTICOMBAT);
        initializeWithEmptyLists(NOT_MULTICOMBAT);
        initializeWithEmptyLists(ROUGH_WILDERNESS);
        initializeWithEmptyLists(WILDERNESS_LEVEL_LINES);
        initializeWithEmptyLists(DEADMAN_SAFE_ZONES);
        initializeWithEmptyLists(PVP_WORLD_SAFE_ZONES);
        initializeWithEmptyLists(SINGLES_PLUS_LIST);

        defineMulticombatAreas();
        defineDeadmanSafeZones();
        definePvpSafeZones();
        defineWilderness();
        defineWildernessLevelLines();

        defineCustomAreas();
    }

    private static void defineCustomAreas() {
        var regionIds = List.of(
            // Araxxor
            14489, 14745, 14746,
            // GWD
            4674, 4675, 4676, 4677
        );
        regionIds.forEach(regionId -> addPolygonOnPlane(MULTICOMBAT, 0,
            ((regionId >> 8) << 6), ((regionId & 0xFF) << 6),
            (((regionId >> 8) + 1) << 6), ((regionId & 0xFF) << 6),
            (((regionId >> 8) + 1) << 6), (((regionId & 0xFF) + 1) << 6),
            ((regionId >> 8) << 6), (((regionId & 0xFF) + 1) << 6)));


        //TODO: Custom areas by Kris
        //The Mimic
        addPolygonOnPlane(MULTICOMBAT, 1,
                2710, 4311,
                2730, 4311,
                2730, 4327,
                2710, 4327);

        //Demonic Gorillas
        addPolygonOnPlane(MULTICOMBAT, 0,
                2091, 5632,
                2048, 5632,
                2048, 5696,
                2176, 5696,
                2176, 5665,
                2114, 5665,
                2114, 5666,
                2091, 5666);

        //Monkey madness dungeon.
        addPolygonOnPlane(MULTICOMBAT, 0,
                2688, 9151,
                2688, 9088,
                2816, 9088,
                2816, 9216,
                2754, 9216,
                2754, 9151);

        addPolygonOnPlane(MULTICOMBAT, 0,
                1235, 10076,
                1260, 10076,
                1260, 10100,
                1235, 10100);

        //Raids
        addPolygonOnPlanes(MULTICOMBAT, new int[]{0, 1, 2, 3},
                3200, 5760,
                3200, 5120,
                3392, 5120,
                3392, 5760);

        //TODO: End by Kris.
    }

    private static void defineMulticombatAreas() {
        // FFA Portal area
        addPolygonOnPlane(MULTICOMBAT, 0,
            3271, 4800,
            3382, 4800,
            3382, 4854,
            3271, 4854);
        // Ferox Area surroundings
        addPolygonOnPlane(MULTICOMBAT, 0,
            3176, 3636,
            3192, 3636,
            3192, 3648,
            3176, 3648);
        addPolygonOnPlane(MULTICOMBAT, 0,
            3152, 3584,
            3192, 3584,
            3192, 3636,
            3187, 3636,
            3187, 3620,
            3162, 3620,
            3161, 3626,
            3152, 3626,
            3152, 3620,
            3146, 3620,
            3146, 3598,
            3147, 3598,
            3147, 3596,
            3149, 3596,
            3149, 3595,
            3150, 3595,
            3150, 3594,
            3151, 3594,
            3151, 3593,
            3152, 3593,
            3152, 3584);

        //Ancient prison
        addPolygonOnPlane(MULTICOMBAT, 0,
                2834, 5184,
                2834, 5244,
                2944, 5244,
                2944, 5184);

        //Wilderness slayer cave
        addPolygonOnPlane(MULTICOMBAT, 0,
                3328, 10048,
                3328, 10175,
                3455, 10175,
                3455, 10048);

        //Monkey madness dungeon.
        addPolygonOnPlane(MULTICOMBAT, 0,
                2688, 9151,
                2688, 9088,
                2816, 9088,
                2816, 9216,
                2754, 9216,
                2754, 9151);

        addPolygonOnPlane(MULTICOMBAT, 0,
                1235, 10076,
                1260, 10076,
                1260, 10100,
                1235, 10100);

        // Main Wilderness
        addPolygonOnPlane(MULTICOMBAT, 0,
                3200, 3968,
                3392, 3968,
                3392, 3840,
                3328, 3840,
                3328, 3520,
                3136, 3520,
                3136, 3584,
                3192, 3584,
                3192, 3752,
                3152, 3752,
                3152, 3840,
                3136, 3840,
                3136, 3872,
                3112, 3872,
                3112, 3880,
                3072, 3880,
                3072, 3896,
                3048, 3896,
                3048, 3872,
                3056, 3872,
                3056, 3864,
                3048, 3864,
                3048, 3856,
                3008, 3856,
                3008, 3904,
                3200, 3904);

        // South of wildy agility training arena
        addPolygonOnPlane(MULTICOMBAT, 0,
                2984, 3928,
                3008, 3928,
                3008, 3912,
                2984, 3912);

        // Wildy zamorak temple
        addPolygonOnPlane(MULTICOMBAT, 0,
                2944, 3832,
                2960, 3832,
                2960, 3816,
                2944, 3816);

        // Wildy bandit camp
        addPolygonOnPlane(MULTICOMBAT, 0,
                3008, 3712,
                3072, 3712,
                3072, 3600,
                3008, 3600);

        // Chaos temple north of Falador
        addPolygonOnPlane(MULTICOMBAT, 0,
                2928, 3520,
                2944, 3520,
                2944, 3512,
                2928, 3512);

        // Burthorpe
        addPolygonOnPlane(MULTICOMBAT, 0,
                2880, 3544,
                2904, 3544,
                2904, 3520,
                2880, 3520);

        // White Wolf Mountain
        addPolygonOnPlane(MULTICOMBAT, 0,
                2880, 3520,
                2816, 3520,
                2816, 3456,
                2880, 3456);

        // Death Plateu
        addPolygonOnPlane(MULTICOMBAT, 0,
                2848, 3608,
                2880, 3608,
                2880, 3600,
                2848, 3600);

        // Trollheim/Godwars
        addPolygonOnPlane(MULTICOMBAT, 0,
                2880, 3776,
                2912, 3776,
                2912, 3696,
                2920, 3696,
                2920, 3688,
                2896, 3688,
                2896, 3696,
                2880, 3696,
                2880, 3728,
                2888, 3728,
                2888, 3744,
                2880, 3744);

        // Northen Rellekka
        addPolygonOnPlane(MULTICOMBAT, 0,
                2656, 3736,
                2704, 3736,
                2704, 3728,
                2712, 3728,
                2712, 3736,
                2736, 3736,
                2736, 3712,
                2656, 3712);

        // Northen Fremennik Isles
        addPolygonOnPlane(MULTICOMBAT, 0,
                2304, 3904,
                2432, 3904,
                2432, 3840,
                2368, 3840,
                2368, 3816,
                2352, 3816,
                2352, 3824,
                2304, 3824);

        // Pirates Cove
        addPolygonOnPlane(MULTICOMBAT, 0,
                2176, 3840,
                2240, 3840,
                2240, 3776,
                2176, 3776);

        // Lunar Isle
        addPolygonOnPlane(MULTICOMBAT, 0,
                2048, 3968,
                2176, 3968,
                2176, 3840,
                2048, 3840);

        // Piscatoris Fishing Colony
        addPolygonOnPlane(MULTICOMBAT, 0,
                2304, 3712,
                2368, 3712,
                2368, 3648,
                2304, 3648);

        // Ranging Guild
        addPolygonOnPlane(MULTICOMBAT, 0,
                2656, 3448,
                2680, 3448,
                2680, 3440,
                2688, 3440,
                2688, 3416,
                2680, 3416,
                2680, 3408,
                2656, 3408,
                2656, 3416,
                2648, 3416,
                2648, 3440,
                2656, 3440);

        // Necromancer house, southeast of Ardy
        addPolygonOnPlane(MULTICOMBAT, 0,
                2656, 3256,
                2680, 3256,
                2680, 3216,
                2664, 3216,
                2664, 3232,
                2656, 3232);

        // Battlefield noth of Tree Gnome Village
        addPolygonOnPlane(MULTICOMBAT, 0,
                2504, 3248,
                2544, 3248,
                2544, 3232,
                2552, 3232,
                2552, 3208,
                2504, 3208);

        // Castle Wars
        addPolygonOnPlane(MULTICOMBAT, 0,
                2368, 3136,
                2432, 3136,
                2432, 3072,
                2368, 3072);

        // Jiggig
        addPolygonOnPlane(MULTICOMBAT, 0,
                2456, 3056,
                2496, 3056,
                2496, 3032,
                2456, 3032);

        // East feldip hills, near rantz
        addPolygonOnPlane(MULTICOMBAT, 0,
                2648, 2976,
                2656, 2976,
                2656, 2952,
                2648, 2952);

        // Ape Atoll
        addPolygonOnPlane(MULTICOMBAT, 0,
                2688, 2816,
                2816, 2816,
                2816, 2688,
                2688, 2688);

        // Pest Control
        addPolygonOnPlane(MULTICOMBAT, 0,
                2624, 2624,
                2688, 2624,
                2688, 2560,
                2624, 2560);

        // Desert Bandit Camp
        addPolygonOnPlane(MULTICOMBAT, 0,
                3152, 3000,
                3192, 3000,
                3192, 2960,
                3152, 2960);

        // Al Kharid
        addPolygonOnPlane(MULTICOMBAT, 0,
                3264, 3200,
                3328, 3200,
                3328, 3136,
                3264, 3136);

        // Wizards Tower
        addPolygonOnPlane(MULTICOMBAT, 0,
                3094, 3176,
                3126, 3176,
                3126, 3144,
                3094, 3144);

        // Draynor Village
        addPolygonOnPlane(MULTICOMBAT, 0,
                3112, 3264,
                3136, 3264,
                3136, 3232,
                3104, 3232,
                3104, 3256,
                3112, 3256);

        // Falador
        addPolygonOnPlane(MULTICOMBAT, 0,
                2944, 3456,
                3008, 3456,
                3008, 3328,
                3016, 3328,
                3016, 3304,
                2944, 3304);

        // Southwest fally castle isn't multicombat downstairs
        addPolygonOnPlane(NOT_MULTICOMBAT, 0,
                2968, 3336,
                2968, 3328,
                2960, 3328,
                2960, 3336);

        // Barbarian Village
        addPolygonOnPlane(MULTICOMBAT, 0,
                3072, 3456,
                3136, 3456,
                3136, 3392,
                3048, 3392,
                3048, 3408,
                3056, 3408,
                3056, 3440,
                3064, 3440,
                3064, 3448,
                3072, 3448);

        // Ammoniate crabs at northwest fossil island
        addPolygonOnPlane(MULTICOMBAT, 0,
                3648, 3885,
                3663, 3885,
                3663, 3882,
                3664, 3882,
                3664, 3872,
                3663, 3872,
                3663, 3868,
                3648, 3868);

        // Ammoniate crabs at north fossil island
        addPolygonOnPlane(MULTICOMBAT, 0,
                3680, 3904,
                3744, 3904,
                3744, 3856,
                3756, 3856,
                3756, 3852,
                3755, 3852,
                3755, 3851,
                3754, 3851,
                3754, 3850,
                3751, 3850,
                3751, 3849,
                3750, 3849,
                3750, 3848,
                3749, 3848,
                3749, 3847,
                3748, 3847,
                3748, 3846,
                3747, 3846,
                3747, 3845,
                3746, 3845,
                3746, 3844,
                3742, 3844,
                3742, 3845,
                3740, 3845,
                3740, 3844,
                3732, 3844,
                3732, 3843,
                3730, 3843,
                3730, 3842,
                3724, 3842,
                3724, 3843,
                3717, 3843,
                3717, 3842,
                3712, 3842,
                3712, 3846,
                3710, 3846,
                3710, 3847,
                3709, 3847,
                3709, 3848,
                3708, 3848,
                3708, 3859,
                3709, 3859,
                3709, 3860,
                3710, 3860,
                3710, 3861,
                3712, 3861,
                3712, 3866,
                3713, 3866,
                3713, 3870,
                3714, 3870,
                3714, 3873,
                3713, 3873,
                3713, 3876,
                3712, 3876,
                3712, 3881,
                3710, 3881,
                3710, 3888,
                3712, 3888,
                3712, 3890,
                3714, 3890,
                3714, 3891,
                3716, 3891,
                3716, 3892,
                3717, 3892,
                3717, 3893,
                3716, 3893,
                3716, 3894,
                3714, 3894,
                3714, 3895,
                3713, 3895,
                3713, 3896,
                3712, 3896,
                3712, 3897,
                3705, 3897,
                3705, 3898,
                3704, 3898,
                3704, 3899,
                3692, 3899,
                3692, 3898,
                3688, 3898,
                3688, 3897,
                3686, 3897,
                3686, 3896,
                3680, 3896);

        // Zeah, southwest of Wintertodt, snowy area with ice giants and wolves
        addPolygonOnPlane(MULTICOMBAT, 0,
                1540, 3898,
                1543, 3898,
                1543, 3901,
                1546, 3901,
                1546, 3903,
                1547, 3903,
                1547, 3904,
                1550, 3904,
                1550, 3903,
                1553, 3903,
                1553, 3904,
                1559, 3904,
                1559, 3902,
                1564, 3902,
                1564, 3903,
                1565, 3903,
                1565, 3904,
                1568, 3904,
                1568, 3903,
                1569, 3903,
                1569, 3902,
                1570, 3902,
                1570, 3901,
                1573, 3901,
                1573, 3898,
                1577, 3898,
                1577, 3899,
                1578, 3899,
                1578, 3902,
                1579, 3902,
                1579, 3903,
                1584, 3903,
                1584, 3902,
                1586, 3902,
                1586, 3901,
                1590, 3901,
                1590, 3891,
                1588, 3891,
                1588, 3887,
                1572, 3887,
                1572, 3872,
                1567, 3872,
                1567, 3868,
                1563, 3868,
                1563, 3867,
                1558, 3867,
                1558, 3868,
                1557, 3868,
                1557, 3870,
                1549, 3870,
                1549, 3874,
                1545, 3874,
                1545, 3876,
                1543, 3876,
                1543, 3877,
                1542, 3877,
                1542, 3879,
                1541, 3879,
                1541, 3882,
                1539, 3882,
                1539, 3887,
                1540, 3887,
                1540, 3888,
                1539, 3888,
                1539, 3894,
                1540, 3894);

        // Zeah arceuus area
        addPolygonOnPlane(MULTICOMBAT, 0,
                1664, 3776,
                1664, 3785,
                1667, 3785,
                1667, 3805,
                1671, 3805,
                1671, 3811,
                1675, 3811,
                1675, 3819,
                1690, 3819,
                1690, 3814,
                1695, 3814,
                1695, 3806,
                1719, 3806,
                1719, 3787,
                1725, 3787,
                1725, 3778,
                1711, 3778,
                1711, 3776);

        // Arceuus teletab-making house
        addPolygonOnPlane(MULTICOMBAT, 0,
                1667, 3772,
                1679, 3772,
                1679, 3775,
                1691, 3775,
                1691, 3761,
                1679, 3761,
                1679, 3764,
                1667, 3764);
        // Next house east
        addPolygonOnPlane(MULTICOMBAT, 0,
                1696, 3775,
                1708, 3775,
                1708, 3763,
                1696, 3763);
        // Next house east
        addPolygonOnPlane(MULTICOMBAT, 0,
                1713, 3775,
                1727, 3775,
                1727, 3763,
                1724, 3763,
                1724, 3752,
                1716, 3752,
                1716, 3763,
                1713, 3763);
        // Arceuus rune shop house
        addPolygonOnPlane(MULTICOMBAT, 0,
                1716, 3750,
                1728, 3750,
                1728, 3736,
                1716, 3736);
        // Arceuus general store house
        addPolygonOnPlane(MULTICOMBAT, 0,
                1717, 3732,
                1725, 3732,
                1725, 3715,
                1715, 3715,
                1715, 3725,
                1717, 3725);
        // Arceuus pub
        addPolygonOnPlane(MULTICOMBAT, 0,
                1683, 3732,
                1691, 3732,
                1691, 3725,
                1697, 3725,
                1697, 3730,
                1703, 3730,
                1703, 3712,
                1683, 3712);
        // Arceuus staff store
        addPolygonOnPlane(MULTICOMBAT, 0,
                1664, 3732,
                1676, 3732,
                1676, 3720,
                1664, 3720);
        // Next house to the west
        addPolygonOnPlane(MULTICOMBAT, 0,
                1647, 3738,
                1655, 3738,
                1655, 3726,
                1658, 3726,
                1658, 3714,
                1644, 3714,
                1644, 3726,
                1647, 3726);
        // Next house to the north
        addPolygonOnPlane(MULTICOMBAT, 0,
                1647, 3762,
                1657, 3762,
                1657, 3752,
                1655, 3752,
                1655, 3745,
                1647, 3745);

        // Arceuus house magic trees
        addPolygonOnPlane(MULTICOMBAT, 0,
                1682, 3755,
                1692, 3755,
                1692, 3745,
                1690, 3745,
                1690, 3738,
                1682, 3738);
        // West of that ^
        addPolygonOnPlane(MULTICOMBAT, 0,
                1667, 3756,
                1675, 3756,
                1675, 3740,
                1665, 3740,
                1665, 3746,
                1667, 3746);

        // This one goes through western piscarilius, northen hosidius
        // and southwestern arceuus
        addPolygonOnPlane(MULTICOMBAT, 0,
                1728, 3808,
                1792, 3808,
                1792, 3764,
                1856, 3764,
                1856, 3712,
                1792, 3712,
                1792, 3648,
                1664, 3648,
                1664, 3706,
                1665, 3706,
                1665, 3705,
                1668, 3705,
                1668, 3706,
                1671, 3706,
                1671, 3705,
                1675, 3705,
                1675, 3704,
                1683, 3704,
                1683, 3701,
                1684, 3701,
                1684, 3700,
                1686, 3700,
                1686, 3702,
                1687, 3702,
                1687, 3700,
                1688, 3700,
                1688, 3701,
                1690, 3701,
                1690, 3703,
                1689, 3703,
                1689, 3704,
                1690, 3704,
                1690, 3705,
                1704, 3705,
                1704, 3707,
                1706, 3707,
                1706, 3712,
                1711, 3712,
                1711, 3711,
                1710, 3711,
                1710, 3710,
                1712, 3710,
                1712, 3707,
                1728, 3707);

        // Kourend castle
        addPolygonOnPlane(MULTICOMBAT, 0,
                1614, 3691,
                1619, 3691,
                1619, 3690,
                1620, 3690,
                1620, 3689,
                1653, 3689,
                1653, 3690,
                1654, 3690,
                1654, 3691,
                1657, 3691,
                1657, 3690,
                1658, 3690,
                1658, 3689,
                1659, 3689,
                1659, 3686,
                1658, 3686,
                1658, 3685,
                1657, 3685,
                1657, 3662,
                1658, 3662,
                1658, 3661,
                1659, 3661,
                1659, 3658,
                1658, 3658,
                1658, 3657,
                1657, 3657,
                1657, 3656,
                1654, 3656,
                1654, 3657,
                1653, 3657,
                1653, 3658,
                1620, 3658,
                1620, 3657,
                1619, 3657,
                1619, 3656,
                1614, 3656,
                1614, 3657,
                1613, 3657,
                1613, 3661,
                1612, 3661,
                1612, 3662,
                1611, 3662,
                1611, 3663,
                1600, 3663,
                1600, 3662,
                1599, 3662,
                1599, 3661,
                1594, 3661,
                1594, 3662,
                1593, 3662,
                1593, 3685,
                1594, 3685,
                1594, 3686,
                1599, 3686,
                1599, 3685,
                1600, 3685,
                1600, 3684,
                1611, 3684,
                1611, 3685,
                1612, 3685,
                1612, 3686,
                1613, 3686,
                1613, 3690,
                1614, 3690);

        // Western hosidius area, including woodcutting guild and western sand crabs
        addPolygonOnPlane(MULTICOMBAT, 0,
                1650, 3648,
                1664, 3648,
                1664, 3520,
                1689, 3520,
                1689, 3496,
                1707, 3496,
                1707, 3485,
                1708, 3485,
                1708, 3484,
                1710, 3484,
                1710, 3483,
                1713, 3483,
                1713, 3482,
                1720, 3482,
                1720, 3481,
                1721, 3481,
                1721, 3480,
                1722, 3480,
                1722, 3479,
                1723, 3479,
                1723, 3478,
                1724, 3478,
                1724, 3477,
                1726, 3477,
                1726, 3476,
                1728, 3476,
                1728, 3472,
                1708, 3472,
                1708, 3456,
                1600, 3456,
                1600, 3584,
                1608, 3584,
                1608, 3616,
                1650, 3616);

        // Hosidius sand crabs
        addPolygonOnPlane(MULTICOMBAT, 0,
                1740, 3478,
                1741, 3478,
                1741, 3479,
                1745, 3479,
                1745, 3480,
                1751, 3480,
                1751, 3479,
                1752, 3479,
                1752, 3478,
                1753, 3478,
                1753, 3477,
                1755, 3477,
                1755, 3476,
                1757, 3476,
                1757, 3475,
                1758, 3475,
                1758, 3474,
                1759, 3474,
                1759, 3473,
                1779, 3473,
                1779, 3474,
                1781, 3474,
                1781, 3475,
                1786, 3475,
                1786, 3476,
                1800, 3476,
                1800, 3475,
                1805, 3475,
                1805, 3474,
                1807, 3474,
                1807, 3473,
                1808, 3473,
                1808, 3472,
                1810, 3472,
                1810, 3471,
                1833, 3471,
                1833, 3470,
                1834, 3470,
                1834, 3469,
                1852, 3469,
                1852, 3449,
                1792, 3449,
                1792, 3424,
                1800, 3424,
                1800, 3449,
                1800, 3400,
                1728, 3400,
                1728, 3462,
                1729, 3462,
                1729, 3466,
                1730, 3466,
                1730, 3469,
                1731, 3469,
                1731, 3470,
                1732, 3470,
                1732, 3471,
                1733, 3471,
                1733, 3473,
                1734, 3473,
                1734, 3474,
                1736, 3474,
                1736, 3475,
                1737, 3475,
                1737, 3476,
                1738, 3476,
                1738, 3477,
                1740, 3477);

        // Apparently there is a 1x1 single zone on the sand crab island
        addPolygonOnPlane(NOT_MULTICOMBAT, 0,
                1777, 3416,
                1777, 3417,
                1778, 3417,
                1778, 3416);

        // Summern hosidius area
        addPolygonOnPlane(MULTICOMBAT, 0,
                1834, 3584,
                1888, 3584,
                1888, 3528,
                1856, 3528,
                1856, 3520,
                1834, 3520,
                1834, 3522,
                1833, 3522,
                1833, 3535,
                1834, 3535,
                1834, 3538,
                1835, 3538,
                1835, 3539,
                1836, 3539,
                1836, 3540,
                1837, 3540,
                1837, 3541,
                1838, 3541,
                1838, 3542,
                1840, 3542,
                1840, 3543,
                1841, 3543,
                1841, 3545,
                1842, 3545,
                1842, 3546,
                1844, 3546,
                1844, 3547,
                1845, 3547,
                1845, 3548,
                1851, 3548,
                1851, 3551,
                1853, 3551,
                1853, 3563,
                1851, 3563,
                1851, 3566,
                1847, 3566,
                1847, 3567,
                1845, 3567,
                1845, 3568,
                1844, 3568,
                1844, 3569,
                1843, 3569,
                1843, 3571,
                1842, 3571,
                1842, 3573,
                1841, 3573,
                1841, 3574,
                1840, 3574,
                1840, 3575,
                1839, 3575,
                1839, 3576,
                1838, 3576,
                1838, 3577,
                1837, 3577,
                1837, 3578,
                1836, 3578,
                1836, 3579,
                1835, 3579,
                1835, 3581,
                1834, 3581);

        // Summern hosidius area also has a 1x1 multi area
        addPolygonOnPlane(MULTICOMBAT, 0,
                1849, 3563,
                1849, 3564,
                1850, 3564,
                1850, 3563);

        // Hosidius cows/chickens/pigs
        addPolygonOnPlane(MULTICOMBAT, 0,
                1792, 3513,
                1802, 3513,
                1802, 3520,
                1810, 3520,
                1810, 3513,
                1816, 3513,
                1816, 3512,
                1836, 3512,
                1836, 3494,
                1796, 3494,
                1796, 3495,
                1792, 3495);

        // Hosidius southeast of tithe farm
        addPolygonOnPlane(MULTICOMBAT, 0,
                1777, 3597,
                1794, 3597,
                1794, 3561,
                1777, 3561,
                1777, 3591,
                1779, 3591,
                1779, 3592,
                1777, 3592);

        // West of shayzien house
        addPolygonOnPlane(MULTICOMBAT, 0,
                1408, 3584,
                1408, 3582,
                1486, 3582,
                1486, 3568,
                1528, 3568,
                1528, 3520,
                1408, 3520,
                1408, 3464,
                1380, 3464,
                1380, 3486,
                1377, 3486,
                1377, 3488,
                1373, 3488,
                1373, 3492,
                1364, 3492,
                1364, 3512,
                1358, 3512,
                1358, 3520,
                1356, 3520,
                1356, 3532,
                1358, 3532,
                1358, 3540,
                1359, 3540,
                1359, 3542,
                1360, 3542,
                1360, 3557,
                1356, 3557,
                1356, 3560,
                1351, 3560,
                1351, 3570,
                1354, 3570,
                1354, 3581,
                1346, 3581,
                1346, 3584);

        // South of chambers of xeric
        addPolygonOnPlane(MULTICOMBAT, 0,
                1261, 3489,
                1259, 3489,
                1259, 3488,
                1255, 3488,
                1255, 3487,
                1243, 3487,
                1243, 3490,
                1234, 3490,
                1234, 3480,
                1192, 3480,
                1192, 3568,
                1209, 3568,
                1209, 3548,
                1215, 3548,
                1215, 3544,
                1217, 3544,
                1217, 3536,
                1235, 3536,
                1235, 3532,
                1249, 3532,
                1249, 3525,
                1248, 3525,
                1248, 3517,
                1254, 3517,
                1254, 3513,
                1274, 3513,
                1274, 3510,
                1296, 3510,
                1296, 3511,
                1300, 3511,
                1300, 3501,
                1287, 3501,
                1287, 3490,
                1280, 3490,
                1280, 3489,
                1264, 3489,
                1264, 3490,
                1261, 3490);

        // Lizardman shamans
        addPolygonOnPlane(MULTICOMBAT, 0,
                1416, 3728,
                1456, 3728,
                1456, 3688,
                1416, 3688);

        // Other lizardman area at shayzien (west side)
        addPolygonOnPlane(MULTICOMBAT, 0,
                1472, 3712,
                1510, 3712,
                1510, 3702,
                1509, 3702,
                1509, 3701,
                1506, 3701,
                1506, 3696,
                1500, 3696,
                1500, 3680,
                1472, 3680);

        // Other lizardman area at shayzien (east side)
        addPolygonOnPlane(MULTICOMBAT, 0,
                1538, 3704,
                1560, 3704,
                1560, 3672,
                1538, 3672);

        // Lovakengj house
        addPolygonOnPlane(MULTICOMBAT, 0,
                1600, 3712,
                1472, 3712,
                1472, 3840,
                1547, 3840,
                1547, 3816,
                1556, 3816,
                1556, 3809,
                1562, 3809,
                1562, 3800,
                1568, 3800,
                1568, 3793,
                1571, 3793,
                1571, 3816,
                1571, 3776,
                1600, 3776);

        // Shayzien house
        addPolygonOnPlane(MULTICOMBAT, 0,
                1475, 3587,
                1475, 3641,
                1534, 3641,
                1534, 3587);

        // Shayzien house bank is non-multi
        addPolygonOnPlane(NOT_MULTICOMBAT, 0,
                1495, 3622,
                1515, 3622,
                1515, 3612,
                1495, 3612);

        // Shayzien house general store
        addPolygonOnPlane(MULTICOMBAT, 0,
                1539, 3640,
                1551, 3640,
                1551, 3621,
                1539, 3621);

        // Kourend woodland barbarian area
        addPolygonOnPlane(MULTICOMBAT, 0,
                1572, 3442,
                1591, 3442,
                1591, 3424,
                1572, 3424);

        // Catacombs
        addPolygonTo(MULTICOMBAT,
                1600, 9984,
                1600, 10067,
                1628, 10067,
                1628, 10070,
                1639, 10070,
                1639, 10112,
                1730, 10112,
                1730, 9984);

        // Zeah dungeon with sand crabs
        addPolygonTo(MULTICOMBAT,
                1632, 9792,
                1632, 9856,
                1728, 9856,
                1728, 9792);

        // Waterbirth island near the doors where people use rune throwing axes
        addPolygonTo(MULTICOMBAT,
                2536, 10136,
                2536, 10152,
                2552, 10152,
                2552, 10136);

        // Waterbirth island dungeon, on the path to dks
        addPolygonTo(MULTICOMBAT,
                1792, 4352,
                1792, 4416,
                1984, 4416,
                1984, 4352);

        // Dagannoths in lighthouse
        addPolygonTo(MULTICOMBAT,
                2496, 10048,
                2560, 10048,
                2560, 9984,
                2496, 9984);

        // Dagannoth kings (DKs) including slayer only dks
        addPolygonTo(MULTICOMBAT,
                2944, 4352,
                2944, 4480,
                2880, 4480,
                2880, 4352);

        // White wolf mountain dungeon at ice queen
        addPolygonTo(MULTICOMBAT,
                2856, 9928,
                2856, 9968,
                2880, 9968,
                2880, 9928);

        // Kharazi jungle dungeon (in dragon slayer 2 quest)
        addPolygonTo(MULTICOMBAT,
                2816, 9296,
                2880, 9296,
                2880, 9216,
                2816, 9216);

        // Tzhaar, fight pits and inferno area
        addPolygonTo(MULTICOMBAT,
                2368, 5184,
                2560, 5184,
                2560, 5056,
                2368, 5056);

        // Smoke devils
        addPolygonTo(MULTICOMBAT,
                2432, 9408,
                2344, 9408,
                2344, 9472,
                2432, 9472);

        // Kraken
        addPolygonTo(MULTICOMBAT,
                2270, 10045,
                2291, 10045,
                2291, 10022,
                2270, 10022);

        // Giant mole
        addPolygonTo(MULTICOMBAT,
                1728, 5240,
                1792, 5240,
                1792, 5120,
                1728, 5120);

        // Godwars dungeon
        addPolygonTo(MULTICOMBAT,
                2816, 5376,
                2944, 5376,
                2944, 5248,
                2816, 5248);

        // Desert treasure shadow diamond area
        addPolygonTo(MULTICOMBAT,
                2752, 5064,
                2728, 5064,
                2728, 5088,
                2720, 5088,
                2720, 5096,
                2712, 5096,
                2712, 5112,
                2736, 5112,
                2736, 5120,
                2752, 5120);

        // Kalphite slayer area
        addPolygonTo(MULTICOMBAT,
                3264, 9544,
                3344, 9544,
                3344, 9472,
                3264, 9472);

        // Normal kalphite area including kalphite queen
        addPolygonTo(MULTICOMBAT,
                3456, 9536,
                3520, 9536,
                3520, 9472,
                3456, 9472);

        // Tarns lair
        addPolygonTo(MULTICOMBAT,
                3136, 4664,
                3200, 4664,
                3200, 4544,
                3136, 4544);

        // Haunted mine boss area
        addPolygonTo(MULTICOMBAT,
                2752, 4416,
                2752, 4480,
                2816, 4480,
                2816, 4416);

        // Entrance to dorgesh kaan
        addPolygonTo(MULTICOMBAT,
                3328, 9600,
                3312, 9600,
                3312, 9640,
                3304, 9640,
                3304, 9664,
                3328, 9664);

        // Hammerspikes hangout in dwarven mines
        addPolygonTo(MULTICOMBAT,
                2960, 9824,
                2976, 9824,
                2976, 9800,
                2960, 9800);

        // Fremennik isles dungeon
        addPolygonTo(MULTICOMBAT,
                2432, 10304,
                2432, 10240,
                2368, 10240,
                2368, 10304);

        // Varrock sewers
        addPolygonTo(MULTICOMBAT,
                3152, 9920,
                3288, 9920,
                3288, 9856,
                3152, 9856);

        // Stronghold of security 1st floor
        addPolygonTo(MULTICOMBAT,
                1856, 5248,
                1920, 5248,
                1920, 5184,
                1856, 5184);

        // Corp cave
        addPolygonTo(MULTICOMBAT,
                2960, 4400,
                3000, 4400,
                3000, 4368,
                2960, 4368);

        // ZMI altar area
        addPolygonTo(MULTICOMBAT,
                3008, 5632,
                3072, 5632,
                3072, 5568,
                3008, 5568);

        // Dragon slayer 2 zeah underground puzzle
        addPolygonTo(MULTICOMBAT,
                1472, 9984,
                1536, 9984,
                1536, 9920,
                1472, 9920);

        // Wildy revenant caves
        addPolygonTo(SINGLES_PLUS_LIST,
               3262, 10049,
               3191, 10048,
               3138, 10111,
               3147, 10225,
               3268, 10240,
               3262, 10049
        );

        // King black dragon (Kbd)
        addPolygonTo(MULTICOMBAT,
                2240, 4672,
                2240, 4736,
                2304, 4736,
                2304, 4672);

        // Scorpia
        addPolygonTo(MULTICOMBAT,
                3248, 10352,
                3248, 10328,
                3216, 10328,
                3216, 10352);

        // Inside mage bank
        addPolygonTo(MULTICOMBAT,
                2496, 4672,
                2496, 4736,
                2560, 4736,
                2560, 4672);

        // Monkey madness 2
        addPolygonTo(MULTICOMBAT,
                2301, 9126,
                2309, 9277,
                2474, 9279,
                2474, 9134);

        // Wildy godwars dungeon
        addPolygonTo(MULTICOMBAT,
                3072, 10112,
                3008, 10112,
                3008, 10176,
                3048, 10176,
                3048, 10152,
                3056, 10152,
                3056, 10144,
                3064, 10144,
                3064, 10136,
                3072, 10136);

        // Enchanted valley
        addPolygonTo(MULTICOMBAT,
                3008, 4480,
                3008, 4544,
                3072, 4544,
                3072, 4480);

        // Zulrah
        addPolygonTo(MULTICOMBAT,
                2256, 3080,
                2280, 3080,
                2280, 3064,
                2256, 3064);

        // Abyssal sire and abyss
        addPolygonTo(MULTICOMBAT,
                3008, 4736,
                2944, 4736,
                2944, 4864,
                3136, 4864,
                3136, 4736,
                3072, 4736,
                3072, 4800,
                3008, 4800);

        // The Nightmare
        addPolygonTo(MULTICOMBAT,
                3859, 9932,
                3856, 9964,
                3887, 9963,
                3886, 9936);

        // Gauntlet
        addPolygonTo(MULTICOMBAT,
                1856, 5632,
                1856, 5694,
                1983, 5695,
                1983, 5632);

        // Artio
        addPolygonTo(SINGLES_PLUS_LIST,
                1747, 11560,
                1744, 11528,
                1778, 11529,
                1778, 11560);

        // Spindel
        addPolygonTo(SINGLES_PLUS_LIST,
                1607, 11572,
                1607, 11530,
                1656, 11572,
                1656, 11530);

        // Calverion
        addPolygonTo(SINGLES_PLUS_LIST,
                1869, 11561,
                1869, 11529,
                1908, 11561,
                1908, 11529);
    }

    private static void defineDeadmanSafeZones() {
        // Varrock
        addPolygonTo(DEADMAN_SAFE_ZONES,
                3182, 3382,
                3182, 3399,
                3174, 3399,
                3174, 3448,
                3198, 3448,
                3198, 3449,
                3197, 3449,
                3197, 3450,
                3196, 3450,
                3196, 3451,
                3195, 3451,
                3195, 3452,
                3194, 3452,
                3194, 3453,
                3193, 3453,
                3193, 3454,
                3192, 3454,
                3192, 3455,
                3191, 3455,
                3191, 3456,
                3190, 3456,
                3190, 3457,
                3185, 3457,
                3185, 3463,
                3186, 3463,
                3186, 3464,
                3187, 3464,
                3187, 3467,
                3167, 3467,
                3167, 3468,
                3163, 3468,
                3163, 3467,
                3142, 3467,
                3142, 3468,
                3141, 3468,
                3141, 3469,
                3140, 3469,
                3140, 3470,
                3139, 3470,
                3139, 3471,
                3138, 3471,
                3138, 3484,
                3139, 3484,
                3139, 3485,
                3140, 3485,
                3140, 3486,
                3141, 3486,
                3141, 3491,
                3140, 3491,
                3140, 3492,
                3139, 3492,
                3139, 3493,
                3138, 3493,
                3138, 3515,
                3139, 3515,
                3139, 3516,
                3140, 3516,
                3140, 3517,
                3141, 3517,
                3141, 3518,
                3160, 3518,
                3160, 3517,
                3161, 3517,
                3161, 3516,
                3162, 3516,
                3162, 3515,
                3167, 3515,
                3167, 3516,
                3168, 3516,
                3168, 3517,
                3169, 3517,
                3169, 3518,
                3191, 3518,
                3191, 3517,
                3192, 3517,
                3192, 3516,
                3193, 3516,
                3193, 3515,
                3194, 3515,
                3194, 3514,
                3195, 3514,
                3195, 3513,
                3196, 3513,
                3196, 3512,
                3197, 3512,
                3197, 3511,
                3198, 3511,
                3198, 3510,
                3199, 3510,
                3199, 3509,
                3200, 3509,
                3200, 3508,
                3230, 3508,
                3230, 3507,
                3231, 3507,
                3231, 3506,
                3232, 3506,
                3232, 3505,
                3233, 3505,
                3233, 3504,
                3234, 3504,
                3234, 3503,
                3235, 3503,
                3235, 3502,
                3252, 3502,
                3252, 3496,
                3253, 3496,
                3253, 3495,
                3254, 3495,
                3254, 3494,
                3255, 3494,
                3255, 3493,
                3263, 3493,
                3263, 3472,
                3264, 3472,
                3264, 3471,
                3265, 3471,
                3265, 3470,
                3266, 3470,
                3266, 3469,
                3267, 3469,
                3267, 3468,
                3268, 3468,
                3268, 3467,
                3269, 3467,
                3269, 3466,
                3270, 3466,
                3270, 3465,
                3271, 3465,
                3271, 3437,
                3274, 3437,
                3274, 3424,
                3277, 3424,
                3277, 3420,
                3274, 3420,
                3274, 3411,
                3275, 3411,
                3275, 3410,
                3276, 3410,
                3276, 3409,
                3277, 3409,
                3277, 3408,
                3288, 3408,
                3288, 3391,
                3289, 3391,
                3289, 3385,
                3290, 3385,
                3290, 3378,
                3289, 3378,
                3289, 3377,
                3288, 3377,
                3288, 3376,
                3265, 3376,
                3265, 3380,
                3253, 3380,
                3253, 3382,
                3245, 3382,
                3245, 3380,
                3242, 3380,
                3242, 3382,
                3239, 3382,
                3239, 3381,
                3209, 3381,
                3209, 3382,
                3282, 3382);

        // Lumbridge
        addPolygonTo(DEADMAN_SAFE_ZONES,
                3201, 3257,
                3213, 3257,
                3213, 3264,
                3233, 3264,
                3233, 3257,
                3235, 3257,
                3235, 3241,
                3237, 3241,
                3237, 3237,
                3239, 3237,
                3239, 3231,
                3243, 3231,
                3243, 3220,
                3253, 3220,
                3253, 3217,
                3256, 3217,
                3256, 3212,
                3259, 3212,
                3259, 3190,
                3247, 3190,
                3247, 3191,
                3238, 3191,
                3238, 3195,
                3230, 3195,
                3230, 3201,
                3228, 3201,
                3228, 3202,
                3227, 3202,
                3227, 3205,
                3228, 3205,
                3228, 3207,
                3225, 3207,
                3225, 3206,
                3224, 3206,
                3224, 3205,
                3223, 3205,
                3223, 3204,
                3222, 3204,
                3222, 3203,
                3215, 3203,
                3215, 3202,
                3214, 3202,
                3214, 3201,
                3203, 3201,
                3203, 3202,
                3202, 3202,
                3202, 3203,
                3201, 3203,
                3201, 3217,
                3199, 3217,
                3199, 3220,
                3201, 3220);

        // Falador
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2986, 3395,
                2986, 3394,
                2987, 3394,
                2987, 3393,
                2996, 3393,
                2996, 3394,
                3002, 3394,
                3002, 3395,
                3009, 3395,
                3009, 3394,
                3010, 3394,
                3010, 3393,
                3011, 3393,
                3011, 3392,
                3021, 3392,
                3021, 3391,
                3022, 3391,
                3022, 3390,
                3041, 3390,
                3041, 3389,
                3047, 3389,
                3047, 3390,
                3062, 3390,
                3062, 3389,
                3063, 3389,
                3063, 3388,
                3064, 3388,
                3064, 3387,
                3065, 3387,
                3065, 3386,
                3066, 3386,
                3066, 3368,
                3065, 3368,
                3065, 3367,
                3064, 3367,
                3064, 3366,
                3063, 3366,
                3063, 3365,
                3062, 3365,
                3062, 3364,
                3061, 3364,
                3061, 3363,
                3060, 3363,
                3060, 3331,
                3061, 3331,
                3061, 3328,
                3058, 3328,
                3058, 3329,
                3025, 3329,
                3025, 3328,
                3024, 3328,
                3024, 3327,
                3016, 3327,
                3016, 3326,
                3015, 3326,
                3015, 3325,
                3014, 3325,
                3014, 3324,
                3013, 3324,
                3013, 3323,
                3008, 3323,
                3008, 3324,
                3006, 3324,
                3006, 3323,
                3002, 3323,
                3002, 3322,
                3001, 3322,
                3001, 3321,
                3000, 3321,
                3000, 3320,
                2999, 3320,
                2999, 3319,
                2998, 3319,
                2998, 3318,
                2997, 3318,
                2997, 3317,
                2996, 3317,
                2996, 3316,
                2992, 3316,
                2992, 3315,
                2991, 3315,
                2991, 3314,
                2990, 3314,
                2990, 3313,
                2989, 3313,
                2989, 3312,
                2988, 3312,
                2988, 3311,
                2987, 3311,
                2987, 3310,
                2986, 3310,
                2986, 3309,
                2966, 3309,
                2966, 3310,
                2956, 3310,
                2956, 3311,
                2941, 3311,
                2941, 3312,
                2940, 3312,
                2940, 3320,
                2936, 3320,
                2936, 3354,
                2937, 3354,
                2937, 3357,
                2936, 3357,
                2936, 3389,
                2937, 3389,
                2937, 3390,
                2938, 3390,
                2938, 3391,
                2939, 3391,
                2939, 3392,
                2940, 3392,
                2940, 3393,
                2943, 3393,
                2943, 3394,
                2944, 3394,
                2944, 3395,
                2950, 3395,
                2950, 3394,
                2956, 3394,
                2956, 3395);

        // Port phasmatys
        addPolygonTo(DEADMAN_SAFE_ZONES,
                3650, 3456,
                3650, 3472,
                3651, 3472,
                3651, 3473,
                3652, 3473,
                3652, 3474,
                3653, 3474,
                3653, 3507,
                3654, 3507,
                3654, 3508,
                3668, 3508,
                3668, 3509,
                3669, 3509,
                3669, 3510,
                3670, 3510,
                3670, 3511,
                3671, 3511,
                3671, 3512,
                3672, 3512,
                3672, 3513,
                3673, 3513,
                3673, 3514,
                3674, 3514,
                3674, 3515,
                3675, 3515,
                3675, 3516,
                3676, 3516,
                3676, 3517,
                3687, 3517,
                3687, 3494,
                3690, 3494,
                3690, 3493,
                3696, 3493,
                3696, 3482,
                3699, 3482,
                3699, 3481,
                3712, 3481,
                3712, 3456);

        // Sophanem
        addPolygonTo(DEADMAN_SAFE_ZONES,
                3274, 2752,
                3274, 2784,
                3277, 2784,
                3277, 2786,
                3274, 2786,
                3274, 2789,
                3272, 2789,
                3272, 2810,
                3322, 2810,
                3322, 2752);

        // Ardy
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2560, 3256,
                2560, 3264,
                2559, 3264,
                2559, 3328,
                2560, 3328,
                2560, 3339,
                2561, 3339,
                2561, 3340,
                2562, 3340,
                2562, 3341,
                2563, 3341,
                2563, 3342,
                2616, 3342,
                2616, 3341,
                2617, 3341,
                2617, 3340,
                2669, 3340,
                2669, 3339,
                2670, 3339,
                2670, 3338,
                2671, 3338,
                2671, 3337,
                2672, 3337,
                2672, 3336,
                2673, 3336,
                2673, 3335,
                2674, 3335,
                2674, 3334,
                2683, 3334,
                2683, 3333,
                2684, 3333,
                2684, 3332,
                2685, 3332,
                2685, 3331,
                2686, 3331,
                2686, 3330,
                2687, 3330,
                2687, 3329,
                2688, 3329,
                2688, 3264,
                2638, 3264,
                2638, 3263,
                2625, 3263,
                2625, 3264,
                2611, 3264,
                2611, 3257,
                2602, 3257,
                2602, 3264,
                2587, 3264,
                2587, 3263,
                2586, 3263,
                2586, 3262,
                2584, 3262,
                2584, 3261,
                2583, 3261,
                2583, 3260,
                2582, 3260,
                2582, 3259,
                2581, 3259,
                2581, 3258,
                2572, 3258,
                2572, 3260,
                2571, 3260,
                2571, 3261,
                2566, 3261,
                2566, 3260,
                2565, 3260,
                2565, 3259,
                2564, 3259,
                2564, 3256);

        // Yanille
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2613, 3103,
                2614, 3103,
                2614, 3102,
                2615, 3102,
                2615, 3101,
                2616, 3101,
                2616, 3100,
                2617, 3100,
                2617, 3099,
                2618, 3099,
                2618, 3098,
                2619, 3098,
                2619, 3097,
                2620, 3097,
                2620, 3075,
                2590, 3075,
                2590, 3074,
                2589, 3074,
                2589, 3073,
                2584, 3073,
                2584, 3074,
                2583, 3074,
                2583, 3075,
                2543, 3075,
                2543, 3076,
                2542, 3076,
                2542, 3077,
                2539, 3077,
                2539, 3107,
                2542, 3107,
                2542, 3108,
                2543, 3108,
                2543, 3109,
                2608, 3109,
                2608, 3108,
                2609, 3108,
                2609, 3107,
                2610, 3107,
                2610, 3106,
                2611, 3106,
                2611, 3105,
                2612, 3105,
                2612, 3104,
                2613, 3104);

        // Gnome stronghold
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2495, 3439,
                2494, 3439,
                2494, 3432,
                2495, 3432,
                2495, 3431,
                2496, 3431,
                2496, 3430,
                2497, 3430,
                2497, 3429,
                2498, 3429,
                2498, 3417,
                2497, 3417,
                2497, 3416,
                2496, 3416,
                2496, 3412,
                2495, 3412,
                2495, 3408,
                2494, 3408,
                2494, 3404,
                2495, 3404,
                2495, 3403,
                2496, 3403,
                2496, 3402,
                2497, 3402,
                2497, 3401,
                2498, 3401,
                2498, 3400,
                2499, 3400,
                2499, 3399,
                2500, 3399,
                2500, 3398,
                2501, 3398,
                2501, 3397,
                2502, 3397,
                2502, 3396,
                2506, 3396,
                2506, 3391,
                2502, 3391,
                2502, 3390,
                2492, 3390,
                2492, 3391,
                2489, 3391,
                2489, 3390,
                2488, 3390,
                2488, 3389,
                2485, 3389,
                2485, 3390,
                2482, 3390,
                2482, 3389,
                2476, 3389,
                2476, 3390,
                2471, 3390,
                2471, 3391,
                2468, 3391,
                2468, 3390,
                2467, 3390,
                2467, 3389,
                2466, 3389,
                2466, 3385,
                2465, 3385,
                2465, 3384,
                2458, 3384,
                2458, 3385,
                2457, 3385,
                2457, 3389,
                2456, 3389,
                2456, 3390,
                2455, 3390,
                2455, 3391,
                2450, 3391,
                2450, 3390,
                2446, 3390,
                2446, 3391,
                2443, 3391,
                2443, 3390,
                2442, 3390,
                2442, 3389,
                2440, 3389,
                2440, 3388,
                2434, 3388,
                2434, 3389,
                2433, 3389,
                2433, 3390,
                2432, 3390,
                2432, 3391,
                2428, 3391,
                2428, 3392,
                2427, 3392,
                2427, 3393,
                2420, 3393,
                2420, 3394,
                2419, 3394,
                2419, 3395,
                2418, 3395,
                2418, 3396,
                2417, 3396,
                2417, 3397,
                2416, 3397,
                2416, 3399,
                2415, 3399,
                2415, 3400,
                2414, 3400,
                2414, 3408,
                2413, 3408,
                2413, 3409,
                2412, 3409,
                2412, 3410,
                2411, 3410,
                2411, 3411,
                2410, 3411,
                2410, 3412,
                2387, 3412,
                2387, 3407,
                2383, 3407,
                2383, 3408,
                2380, 3408,
                2380, 3409,
                2379, 3409,
                2379, 3410,
                2377, 3410,
                2377, 3411,
                2376, 3411,
                2376, 3413,
                2375, 3413,
                2375, 3417,
                2374, 3417,
                2374, 3418,
                2373, 3418,
                2373, 3419,
                2372, 3419,
                2372, 3420,
                2371, 3420,
                2371, 3421,
                2370, 3421,
                2370, 3422,
                2369, 3422,
                2369, 3433,
                2370, 3433,
                2370, 3434,
                2371, 3434,
                2371, 3444,
                2372, 3444,
                2372, 3445,
                2373, 3445,
                2373, 3446,
                2374, 3446,
                2374, 3447,
                2375, 3447,
                2375, 3459,
                2376, 3459,
                2376, 3460,
                2377, 3460,
                2377, 3461,
                2378, 3461,
                2378, 3462,
                2379, 3462,
                2379, 3463,
                2380, 3463,
                2380, 3464,
                2381, 3464,
                2381, 3476,
                2379, 3476,
                2379, 3477,
                2378, 3477,
                2378, 3478,
                2377, 3478,
                2377, 3485,
                2376, 3485,
                2376, 3486,
                2375, 3486,
                2375, 3499,
                2376, 3499,
                2376, 3500,
                2377, 3500,
                2377, 3507,
                2378, 3507,
                2378, 3508,
                2379, 3508,
                2379, 3509,
                2380, 3509,
                2380, 3521,
                2382, 3521,
                2382, 3522,
                2384, 3522,
                2384, 3523,
                2393, 3523,
                2393, 3524,
                2399, 3524,
                2399, 3525,
                2404, 3525,
                2404, 3524,
                2405, 3524,
                2405, 3523,
                2407, 3523,
                2407, 3522,
                2415, 3522,
                2415, 3521,
                2425, 3521,
                2425, 3522,
                2427, 3522,
                2427, 3523,
                2430, 3523,
                2430, 3522,
                2431, 3522,
                2431, 3521,
                2432, 3521,
                2432, 3520,
                2448, 3520,
                2448, 3517,
                2454, 3517,
                2454, 3516,
                2455, 3516,
                2455, 3515,
                2456, 3515,
                2456, 3514,
                2457, 3514,
                2457, 3513,
                2460, 3513,
                2460, 3512,
                2461, 3512,
                2461, 3511,
                2465, 3511,
                2465, 3510,
                2468, 3510,
                2468, 3511,
                2472, 3511,
                2472, 3512,
                2473, 3512,
                2473, 3513,
                2475, 3513,
                2475, 3514,
                2476, 3514,
                2476, 3515,
                2477, 3515,
                2477, 3516,
                2478, 3516,
                2478, 3517,
                2483, 3517,
                2483, 3516,
                2487, 3516,
                2487, 3515,
                2488, 3515,
                2488, 3512,
                2487, 3512,
                2487, 3509,
                2488, 3509,
                2488, 3508,
                2489, 3508,
                2489, 3507,
                2491, 3507,
                2491, 3506,
                2492, 3506,
                2492, 3505,
                2493, 3505,
                2493, 3499,
                2492, 3499,
                2492, 3498,
                2491, 3498,
                2491, 3497,
                2490, 3497,
                2490, 3495,
                2491, 3495,
                2491, 3494,
                2492, 3494,
                2492, 3493,
                2493, 3493,
                2493, 3485,
                2490, 3485,
                2490, 3484,
                2489, 3484,
                2489, 3483,
                2488, 3483,
                2488, 3482,
                2487, 3482,
                2487, 3481,
                2486, 3481,
                2486, 3474,
                2488, 3474,
                2488, 3471,
                2489, 3471,
                2489, 3470,
                2490, 3470,
                2490, 3460,
                2491, 3460,
                2491, 3456,
                2496, 3456,
                2496, 3440,
                2495, 3440);

        // Rellekka
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2620, 3682,
                2624, 3682,
                2624, 3683,
                2625, 3683,
                2625, 3687,
                2629, 3687,
                2629, 3686,
                2630, 3686,
                2630, 3685,
                2632, 3685,
                2632, 3686,
                2636, 3686,
                2636, 3692,
                2645, 3692,
                2645, 3695,
                2647, 3695,
                2647, 3696,
                2649, 3696,
                2649, 3702,
                2650, 3702,
                2650, 3703,
                2651, 3703,
                2651, 3704,
                2652, 3704,
                2652, 3711,
                2653, 3711,
                2653, 3712,
                2691, 3712,
                2691, 3709,
                2692, 3709,
                2692, 3707,
                2693, 3707,
                2693, 3703,
                2692, 3703,
                2692, 3701,
                2691, 3701,
                2691, 3699,
                2690, 3699,
                2690, 3695,
                2691, 3695,
                2691, 3693,
                2692, 3693,
                2692, 3691,
                2693, 3691,
                2693, 3685,
                2692, 3685,
                2692, 3683,
                2691, 3683,
                2691, 3681,
                2690, 3681,
                2690, 3680,
                2689, 3680,
                2689, 3672,
                2690, 3672,
                2690, 3671,
                2691, 3671,
                2691, 3666,
                2690, 3666,
                2690, 3664,
                2689, 3664,
                2689, 3660,
                2690, 3660,
                2690, 3658,
                2691, 3658,
                2691, 3656,
                2692, 3656,
                2692, 3654,
                2693, 3654,
                2693, 3651,
                2692, 3651,
                2692, 3649,
                2690, 3649,
                2690, 3648,
                2688, 3648,
                2688, 3647,
                2686, 3647,
                2686, 3646,
                2673, 3646,
                2673, 3645,
                2636, 3645,
                2636, 3647,
                2627, 3647,
                2627, 3648,
                2625, 3648,
                2625, 3649,
                2624, 3649,
                2624, 3650,
                2622, 3650,
                2622, 3651,
                2620, 3651,
                2620, 3652,
                2618, 3652,
                2618, 3653,
                2616, 3653,
                2616, 3654,
                2609, 3654,
                2609, 3655,
                2607, 3655,
                2607, 3656,
                2603, 3656,
                2603, 3657,
                2602, 3657,
                2602, 3658,
                2601, 3658,
                2601, 3663,
                2602, 3663,
                2602, 3664,
                2603, 3664,
                2603, 3665,
                2604, 3665,
                2604, 3666,
                2605, 3666,
                2605, 3667,
                2606, 3667,
                2606, 3671,
                2609, 3671,
                2609, 3672,
                2610, 3672,
                2610, 3673,
                2611, 3673,
                2611, 3675,
                2612, 3675,
                2612, 3676,
                2614, 3676,
                2614, 3677,
                2616, 3677,
                2616, 3679,
                2618, 3679,
                2618, 3681,
                2620, 3681);

        // Jatizo
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2407, 3797,
                2407, 3793,
                2399, 3793,
                2399, 3792,
                2391, 3792,
                2391, 3791,
                2386, 3791,
                2386, 3796,
                2388, 3796,
                2388, 3802,
                2386, 3802,
                2386, 3807,
                2388, 3807,
                2388, 3809,
                2402, 3809,
                2402, 3819,
                2406, 3819,
                2406, 3824,
                2408, 3824,
                2408, 3826,
                2413, 3826,
                2413, 3824,
                2419, 3824,
                2419, 3826,
                2424, 3826,
                2424, 3821,
                2423, 3821,
                2423, 3798,
                2422, 3798,
                2422, 3797);

        // Neitiznot
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2329, 3812,
                2333, 3812,
                2333, 3813,
                2334, 3813,
                2334, 3814,
                2335, 3814,
                2335, 3815,
                2338, 3815,
                2338, 3816,
                2339, 3816,
                2339, 3817,
                2368, 3817,
                2368, 3776,
                2352, 3776,
                2352, 3796,
                2344, 3796,
                2344, 3795,
                2331, 3795,
                2331, 3797,
                2330, 3797,
                2330, 3798,
                2329, 3798);

        // Pest control
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2624, 2688,
                2688, 2688,
                2688, 2624,
                2624, 2624);

        // Tutorial island
        addPolygonTo(DEADMAN_SAFE_ZONES,
                3052, 3135,
                3156, 3135,
                3156, 3057,
                3052, 3057);

        // Camelot bank
        addPolygonOnPlanes(DEADMAN_SAFE_ZONES, new int[]{0, 1},
                2724, 3487,
                2724, 3490,
                2721, 3490,
                2721, 3494,
                2719, 3494,
                2719, 3497,
                2721, 3497,
                2721, 3498,
                2731, 3498,
                2731, 3490,
                2728, 3490,
                2728, 3487);

        // Catherby bank
        addPolygonTo(DEADMAN_SAFE_ZONES,
                2806, 3438,
                2806, 3446,
                2813, 3446,
                2813, 3438);

        // Kourend castle
        addPolygonTo(DEADMAN_SAFE_ZONES,
                1627, 3658,
                1620, 3658,
                1620, 3657,
                1619, 3657,
                1619, 3656,
                1614, 3656,
                1614, 3657,
                1613, 3657,
                1613, 3661,
                1612, 3661,
                1612, 3662,
                1611, 3662,
                1611, 3663,
                1600, 3663,
                1600, 3662,
                1599, 3662,
                1599, 3661,
                1594, 3661,
                1594, 3662,
                1593, 3662,
                1593, 3685,
                1594, 3685,
                1594, 3686,
                1599, 3686,
                1599, 3685,
                1600, 3685,
                1600, 3684,
                1611, 3684,
                1611, 3685,
                1612, 3685,
                1612, 3686,
                1613, 3686,
                1613, 3690,
                1614, 3690,
                1614, 3691,
                1619, 3691,
                1619, 3690,
                1620, 3690,
                1620, 3689,
                1630, 3689,
                1630, 3686,
                1620, 3686,
                1620, 3685,
                1619, 3685,
                1619, 3683,
                1620, 3683,
                1620, 3682,
                1621, 3682,
                1621, 3681,
                1622, 3681,
                1622, 3680,
                1623, 3680,
                1623, 3679,
                1624, 3679,
                1624, 3668,
                1623, 3668,
                1623, 3667,
                1622, 3667,
                1622, 3666,
                1621, 3666,
                1621, 3665,
                1620, 3665,
                1620, 3664,
                1619, 3664,
                1619, 3662,
                1620, 3662,
                1620, 3661,
                1627, 3661);
    }

    private static void definePvpSafeZones() {
        // Ferox inside
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                3143, 3618,
                3138, 3618,
                3138, 3616,
                3131, 3617,
                3131, 3618,
                3126, 3618,
                3126, 3618,
                3126, 3622,
                3123, 3622,
                3123, 3633,
                3125, 3633,
                3125, 3640,
                3138, 3639,
                3138, 3647,
                3156, 3647,
                3156, 3640,
                3155, 3640,
                3155, 3636,
                3155, 3633,
                3156, 3626,
                3152, 3626,
                3152, 3623,
                3149, 3623,
                3149, 3620,
                3143, 3620,
                3143, 3618);

        // Ferix outside too
		/*addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
				3144, 3615,
				3141, 3615,
				3141, 3610,
				3129, 3610,
				3129, 3616,
				3124, 3616,
				3124, 3621,
				3122, 3621,
				3122, 3622,
				3121, 3622,
				3121, 3623,
				3118, 3623,
				3118, 3635,
				3120, 3636,
				3121, 3635,
				3121, 3637,
				3122, 3636,
				3122, 3638,
				3123, 3637,
				3123, 3644,
				3136, 3644,
				3136, 3646,
				3137, 3646,
				3137, 3647,
				3157, 3647,
				3157, 3644,
				3158, 3644,
				3158, 3643,
				3160, 3643,
				3161, 3634,
				3160, 3627,
				3153, 3627,
				3152, 3623,
				3148, 3620,
				3146, 3620,
				3145, 3615);*/

        // Grand exchange
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3159, 3473,
                3159, 3474,
                3157, 3474,
                3157, 3475,
                3155, 3475,
                3155, 3476,
                3153, 3476,
                3153, 3477,
                3152, 3477,
                3152, 3478,
                3151, 3478,
                3151, 3480,
                3150, 3480,
                3150, 3482,
                3149, 3482,
                3149, 3484,
                3148, 3484,
                3148, 3496,
                3149, 3496,
                3149, 3498,
                3150, 3498,
                3150, 3500,
                3151, 3500,
                3151, 3502,
                3152, 3502,
                3152, 3503,
                3153, 3503,
                3153, 3504,
                3155, 3504,
                3155, 3505,
                3157, 3505,
                3157, 3506,
                3159, 3506,
                3159, 3507,
                3171, 3507,
                3171, 3506,
                3173, 3506,
                3173, 3505,
                3175, 3505,
                3175, 3504,
                3177, 3504,
                3177, 3503,
                3178, 3503,
                3178, 3502,
                3179, 3502,
                3179, 3500,
                3180, 3500,
                3180, 3498,
                3181, 3498,
                3181, 3496,
                3182, 3496,
                3182, 3484,
                3181, 3484,
                3181, 3482,
                3180, 3482,
                3180, 3480,
                3179, 3480,
                3179, 3478,
                3178, 3478,
                3178, 3477,
                3177, 3477,
                3177, 3476,
                3175, 3476,
                3175, 3475,
                3173, 3475,
                3173, 3474,
                3171, 3474,
                3171, 3473);

        // Edgeville
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3091, 3488,
                3091, 3493,
                3090, 3493,
                3090, 3498,
                3091, 3498,
                3091, 3500,
                3099, 3500,
                3099, 3488);

        // Fally west bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2943, 3368,
                2943, 3374,
                2948, 3374,
                2948, 3370,
                2950, 3370,
                2950, 3366,
                2949, 3366,
                2949, 3359,
                2945, 3359,
                2945, 3362,
                2946, 3362,
                2946, 3366,
                2945, 3366,
                2945, 3368);

        // Fally east bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3009, 3353,
                3009, 3359,
                3019, 3359,
                3019, 3357,
                3022, 3357,
                3022, 3353);

        // Fally castle
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2964, 3354,
                2966, 3354,
                2966, 3352,
                2967, 3352,
                2967, 3349,
                2976, 3349,
                2976, 3348,
                2977, 3348,
                2977, 3347,
                2981, 3347,
                2981, 3343,
                2982, 3343,
                2982, 3339,
                2981, 3339,
                2981, 3337,
                2967, 3337,
                2967, 3330,
                2963, 3330,
                2963, 3331,
                2962, 3331,
                2962, 3332,
                2961, 3332,
                2961, 3334,
                2964, 3334,
                2964, 3335,
                2965, 3335,
                2965, 3343,
                2964, 3343,
                2964, 3344,
                2961, 3344,
                2961, 3350,
                2963, 3350,
                2963, 3352,
                2964, 3352);

        // Varrock east bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3250, 3425,
                3258, 3425,
                3258, 3416,
                3250, 3416);

        // Varrock west bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3180, 3433,
                3180, 3448,
                3191, 3448,
                3191, 3433);

        // Port phasmatys
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3686, 3472,
                3700, 3472,
                3700, 3461,
                3686, 3461);

        // Yanille bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2609, 3088,
                2609, 3098,
                2617, 3098,
                2617, 3088);

        // Ardy east bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2649, 3280,
                2649, 3288,
                2659, 3288,
                2659, 3280);

        // Ardy west bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2612, 3330,
                2612, 3336,
                2615, 3336,
                2615, 3335,
                2619, 3335,
                2619, 3336,
                2622, 3336,
                2622, 3330);

        // Fishing guild bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2593, 3413,
                2588, 3413,
                2588, 3418,
                2583, 3418,
                2583, 3423,
                2590, 3423,
                2590, 3420,
                2593, 3420);

        // Gnome stronghold bank near slayer cave (2nd floor)
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 1,
                2444, 3431,
                2444, 3435,
                2448, 3435,
                2448, 3431,
                2447, 3431,
                2447, 3428,
                2449, 3428,
                2449, 3422,
                2447, 3422,
                2447, 3419,
                2448, 3419,
                2448, 3415,
                2444, 3415,
                2444, 3419,
                2445, 3419,
                2445, 3422,
                2443, 3422,
                2443, 3428,
                2445, 3428,
                2445, 3431);

        // Gnome stronghold bank in grand tree
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 1,
                2456, 3488,
                2452, 3488,
                2452, 3486,
                2450, 3486,
                2450, 3483,
                2451, 3483,
                2451, 3478,
                2448, 3478,
                2448, 3483,
                2449, 3483,
                2449, 3486,
                2447, 3486,
                2447, 3488,
                2443, 3488,
                2443, 3487,
                2438, 3487,
                2438, 3490,
                2443, 3490,
                2443, 3489,
                2447, 3489,
                2447, 3491,
                2449, 3491,
                2449, 3494,
                2448, 3494,
                2448, 3496,
                2451, 3496,
                2451, 3494,
                2450, 3494,
                2450, 3491,
                2452, 3491,
                2452, 3489,
                2456, 3489);

        // Al kharid bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3265, 3161,
                3265, 3174,
                3273, 3174,
                3273, 3161);

        // Shantay pass bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3308, 3119,
                3308, 3125,
                3310, 3125,
                3310, 3119);

        // Nardah bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3431, 2891,
                3431, 2889,
                3427, 2889,
                3427, 2887,
                3424, 2887,
                3424, 2895,
                3431, 2895,
                3431, 2893,
                3432, 2893,
                3432, 2891);

        // Sophanem bank
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                2807, 5158,
                2792, 5158,
                2792, 5175,
                2807, 5175);

        // Canifis bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3509, 3474,
                3509, 3478,
                3508, 3478,
                3508, 3483,
                3509, 3483,
                3509, 3484,
                3517, 3484,
                3517, 3477,
                3516, 3477,
                3516, 3476,
                3513, 3476,
                3513, 3474);

        // Lumbridge castle outside
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3216, 3209,
                3216, 3210,
                3217, 3210,
                3217, 3228,
                3216, 3228,
                3216, 3229,
                3227, 3229,
                3227, 3221,
                3230, 3221,
                3230, 3217,
                3227, 3217,
                3227, 3209);

        // Lumbridge bank upstairs
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 2,
                3211, 3223,
                3211, 3215,
                3207, 3215,
                3207, 3223);

        // Draynor bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3098, 3240,
                3088, 3240,
                3088, 3247,
                3098, 3247);

        // Pest control bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2665, 2656,
                2670, 2656,
                2670, 2651,
                2665, 2651);

        // Shilo village bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2843, 2957,
                2846, 2957,
                2846, 2956,
                2849, 2956,
                2849, 2957,
                2850, 2957,
                2850, 2958,
                2855, 2958,
                2855, 2957,
                2856, 2957,
                2856, 2956,
                2858, 2956,
                2858, 2957,
                2862, 2957,
                2862, 2952,
                2858, 2952,
                2858, 2953,
                2856, 2953,
                2856, 2952,
                2855, 2952,
                2855, 2951,
                2850, 2951,
                2850, 2952,
                2849, 2952,
                2849, 2953,
                2847, 2953,
                2847, 2952,
                2843, 2952);

        // Legends guild bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 2,
                2731, 3374,
                2731, 3383,
                2734, 3383,
                2734, 3374);

        // Legends guild middle floor
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 1,
                2724, 3374,
                2724, 3383,
                2734, 3383,
                2734, 3382,
                2736, 3382,
                2736, 3375,
                2734, 3375,
                2734, 3374);

        // Warriors guild bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2843, 3537,
                2843, 3540,
                2841, 3540,
                2841, 3546,
                2849, 3546,
                2849, 3537,
                2847, 3537,
                2847, 3536,
                2846, 3536,
                2846, 3537);

        // Camelot bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2724, 3487,
                2724, 3490,
                2721, 3490,
                2721, 3494,
                2719, 3494,
                2719, 3497,
                2721, 3497,
                2721, 3498,
                2731, 3498,
                2731, 3490,
                2728, 3490,
                2728, 3487);

        // Camelot respawn point
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2761, 3483,
                2761, 3476,
                2755, 3476,
                2755, 3483);

        // Catherby bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2806, 3438,
                2806, 3446,
                2813, 3446,
                2813, 3438);

        // Barbarian outpost bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2536, 3572,
                2536, 3575,
                2538, 3575,
                2538, 3572);

        // Piscatoris bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2327, 3686,
                2327, 3694,
                2333, 3694,
                2333, 3686);

        // Lletya bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2350, 3161,
                2350, 3165,
                2351, 3165,
                2351, 3167,
                2357, 3167,
                2357, 3165,
                2356, 3165,
                2356, 3164,
                2355, 3164,
                2355, 3161);

        // Castle wars bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2446, 3087,
                2445, 3087,
                2445, 3085,
                2447, 3085,
                2447, 3081,
                2443, 3081,
                2443, 3082,
                2439, 3082,
                2439, 3081,
                2435, 3081,
                2435, 3099,
                2439, 3099,
                2439, 3098,
                2443, 3098,
                2443, 3099,
                2447, 3099,
                2447, 3095,
                2445, 3095,
                2445, 3093,
                2446, 3093);

        // Duel arena bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3380, 3267,
                3380, 3273,
                3381, 3273,
                3381, 3274,
                3385, 3274,
                3385, 3267);

        // Clan wars bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3375, 3165,
                3361, 3165,
                3361, 3173,
                3375, 3173);

        // Lumbridge cellar bank
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                3218, 9622,
                3218, 9624,
                3220, 9624,
                3220, 9622);

        // Dorgesh kaan bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2709, 5348,
                2707, 5348,
                2707, 5345,
                2701, 5345,
                2701, 5347,
                2697, 5347,
                2697, 5353,
                2701, 5353,
                2701, 5355,
                2707, 5355,
                2707, 5350,
                2709, 5350);

        // Keldagrim bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2842, 10204,
                2834, 10204,
                2834, 10216,
                2842, 10216);

        // Tzhaar bank
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                2438, 5176,
                2438, 5180,
                2441, 5180,
                2441, 5182,
                2449, 5182,
                2449, 5181,
                2450, 5181,
                2450, 5180,
                2452, 5180,
                2452, 5175,
                2441, 5175,
                2441, 5176);

        // Inferno bank
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                2542, 5135,
                2542, 5139,
                2539, 5139,
                2539, 5140,
                2538, 5140,
                2538, 5141,
                2537, 5141,
                2537, 5144,
                2541, 5144,
                2541, 5145,
                2543, 5145,
                2543, 5144,
                2544, 5144,
                2544, 5142,
                2545, 5142,
                2545, 5135);

        // Port khazard bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2661, 3160,
                2661, 3163,
                2666, 3163,
                2666, 3160);

        // Corsair cove bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2569, 2863,
                2569, 2868,
                2572, 2868,
                2572, 2863);

        // Burgh de rott bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3495, 3210,
                3495, 3214,
                3501, 3214,
                3501, 3210);

        // Edgeville respawn point
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3092, 3468,
                3092, 3474,
                3098, 3474,
                3098, 3468);

        // Mage bank
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                2529, 4711,
                2529, 4724,
                2548, 4724,
                2548, 4711);

        // Lunar bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2097, 3917,
                2097, 3922,
                2105, 3922,
                2105, 3917);

        // Jatizo bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2414, 3801,
                2414, 3804,
                2420, 3804,
                2420, 3801);

        // Neitiznot bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2334, 3805,
                2334, 3809,
                2340, 3809,
                2340, 3805);

        // Hosidius bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1671, 3558,
                1671, 3577,
                1682, 3577,
                1682, 3558);

        // Woodcutting guild bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1589, 3475,
                1589, 3481,
                1594, 3481,
                1594, 3475);

        // Lands end bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1508, 3415,
                1508, 3424,
                1514, 3424,
                1514, 3415);

        // Chambers of xeric bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1252, 3570,
                1252, 3574,
                1257, 3574,
                1257, 3570);

        // Arceuus bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1621, 3736,
                1621, 3754,
                1627, 3754,
                1627, 3751,
                1633, 3751,
                1633, 3754,
                1639, 3754,
                1639, 3736);

        // Piscarilius bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1794, 3784,
                1794, 3794,
                1812, 3794,
                1812, 3784);

        // Lovakengj bank southeast
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1518, 3735,
                1518, 3744,
                1535, 3744,
                1535, 3735);

        // Lovakenj bank west
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1433, 3820,
                1433, 3837,
                1442, 3837,
                1442, 3820);

        // Lovakenj sulphur mine bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1452, 3855,
                1452, 3860,
                1455, 3860,
                1455, 3855);

        // Blast mine bank southeast
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1500, 3856,
                1500, 3858,
                1503, 3858,
                1503, 3856);

        // Wintertodt bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1638, 3942,
                1638, 3947,
                1642, 3947,
                1642, 3942);

        // Shayzien bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1495, 3612,
                1495, 3622,
                1515, 3622,
                1515, 3612);

        // Hosidius grape farm bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1804, 3571,
                1804, 3572,
                1808, 3572,
                1808, 3571);

        // Hosidius cooking bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                1652, 3605,
                1652, 3615,
                1661, 3615,
                1661, 3605);

        // Ecteria bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                2618, 3893,
                2618, 3897,
                2622, 3897,
                2622, 3893);

        // Mining guild expanded area
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                3018, 9733,
                3021, 9733,
                3021, 9729,
                3022, 9729,
                3022, 9728,
                3023, 9728,
                3023, 9727,
                3025, 9727,
                3025, 9726,
                3026, 9726,
                3026, 9725,
                3030, 9725,
                3030, 9726,
                3032, 9726,
                3032, 9727,
                3035, 9727,
                3035, 9726,
                3038, 9726,
                3038, 9727,
                3041, 9727,
                3041, 9728,
                3042, 9728,
                3042, 9730,
                3045, 9730,
                3045, 9727,
                3047, 9727,
                3047, 9726,
                3048, 9726,
                3048, 9724,
                3052, 9724,
                3052, 9725,
                3053, 9725,
                3053, 9726,
                3055, 9726,
                3055, 9725,
                3056, 9725,
                3056, 9723,
                3057, 9723,
                3057, 9720,
                3056, 9720,
                3056, 9719,
                3054, 9719,
                3054, 9718,
                3052, 9718,
                3052, 9717,
                3050, 9717,
                3050, 9718,
                3045, 9718,
                3045, 9716,
                3044, 9716,
                3044, 9715,
                3041, 9715,
                3041, 9714,
                3039, 9714,
                3039, 9713,
                3037, 9713,
                3037, 9714,
                3036, 9714,
                3036, 9715,
                3034, 9715,
                3034, 9716,
                3029, 9716,
                3029, 9715,
                3028, 9715,
                3028, 9714,
                3026, 9714,
                3026, 9709,
                3027, 9709,
                3027, 9708,
                3028, 9708,
                3028, 9705,
                3029, 9705,
                3029, 9701,
                3028, 9701,
                3028, 9700,
                3027, 9700,
                3027, 9699,
                3023, 9699,
                3023, 9700,
                3019, 9700,
                3019, 9701,
                3018, 9701,
                3018, 9705,
                3019, 9705,
                3019, 9707,
                3020, 9707,
                3020, 9708,
                3021, 9708,
                3021, 9709,
                3022, 9709,
                3022, 9713,
                3021, 9713,
                3021, 9714,
                3019, 9714,
                3019, 9715,
                3018, 9715,
                3018, 9717,
                3015, 9717,
                3015, 9716,
                3013, 9716,
                3013, 9717,
                3012, 9717,
                3012, 9720,
                3013, 9720,
                3013, 9721,
                3015, 9721,
                3015, 9723,
                3016, 9723,
                3016, 9727,
                3017, 9727,
                3017, 9730,
                3018, 9730);

        // Motherlode mine bank
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                3760, 5671,
                3760, 5668,
                3761, 5668,
                3761, 5665,
                3760, 5665,
                3760, 5663,
                3758, 5663,
                3758, 5671);

        // Mos le harmles bank
        addPolygonOnPlane(PVP_WORLD_SAFE_ZONES, 0,
                3679, 2980,
                3679, 2985,
                3681, 2985,
                3681, 2984,
                3682, 2984,
                3682, 2985,
                3684, 2985,
                3684, 2980,
                3682, 2980,
                3682, 2981,
                3681, 2981,
                3681, 2980);

        // Zanaris bank
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                2388, 4454,
                2380, 4454,
                2380, 4463,
                2388, 4463);

        // Woodcuting guild bank underground
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                1550, 9872,
                1550, 9874,
                1553, 9874,
                1553, 9872);

        // Last man standing lobby
        addPolygonTo(PVP_WORLD_SAFE_ZONES,
                3397, 3176,
                3397, 3182,
                3404, 3189,
                3419, 3189,
                3419, 3182,
                3414, 3179,
                3410, 3175,
                3410, 3172,
                3409, 3171,
                3401, 3171,
                3398, 3171,
                3398, 3175);
    }

    private static void defineWilderness() {
        // Above ground
        addPolygonTo(ROUGH_WILDERNESS,
                2944, 3523,
                3392, 3523,
                3392, 3971,
                2944, 3971);

        // Underground
        addPolygonTo(ROUGH_WILDERNESS,
                2944, 9918,
                2944, 10360,
                3264, 10360,
                3264, 9918);
    }

    private static void defineWildernessLevelLines() {
        int wildyLeftX = 2944;
        int wildyRightX = 3392;
        int wildyBottomY = 3525;

        // define wilderness level lines at ground level
        int accumulatedY = 0;
        for (int level = 1; level <= 56; level++) {
            int levelTiles = level == 1 ? 3 : 8;
            // only draw every 2 levels, otherwise lines on two adjacent levels will collide
            // and it will not show up
            if (level % 2 != 0) {
                addPolygonTo(WILDERNESS_LEVEL_LINES,
                        wildyLeftX, wildyBottomY + accumulatedY,
                        wildyRightX, wildyBottomY + accumulatedY,
                        wildyRightX, wildyBottomY + accumulatedY + levelTiles,
                        wildyLeftX, wildyBottomY + accumulatedY + levelTiles);
            }
            accumulatedY += levelTiles;
        }
    }

    private static void addPolygonTo(List<Shape>[] shapes, int... coords) {
        Polygon poly = new Polygon();
        for (int i = 0; i < coords.length; i += 2) {
            poly.addPoint(coords[i], coords[i + 1]);
        }
        for (List<Shape> shape : shapes) {
            shape.add(poly);
        }
    }

    private static void addPolygonOnPlane(List<Shape>[] shapes, int plane, int... coords) {
        Polygon poly = new Polygon();
        int offX = 0;
        int offY = 0;
        for (int i = 0; i < coords.length; i += 2) {
            poly.addPoint(coords[i] + offX, coords[i + 1] + offY);
        }
        shapes[plane].add(poly);
    }

    private static void addPolygonOnPlanes(List<Shape>[] shapes, int[] planes, int... coords) {
        for (int plane : planes) {
            addPolygonOnPlane(shapes, plane, coords);
        }
    }

}
