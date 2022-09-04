package xyz.edge.ac.checks.checkloader;

import xyz.edge.ac.checks.checks.jesus.JesusA;
import xyz.edge.ac.checks.checks.scaffold.ScaffoldX;
import xyz.edge.ac.checks.checks.scaffold.Scaffold2;
import xyz.edge.ac.checks.checks.scaffold.ScaffoldF;
import xyz.edge.ac.checks.checks.scaffold.ScaffoldJ;
import xyz.edge.ac.checks.checks.scaffold.ScaffoldE;
import xyz.edge.ac.checks.checks.scaffold.ScaffoldD;
import xyz.edge.ac.checks.checks.scaffold.ScaffoldC;
import xyz.edge.ac.checks.checks.scaffold.ScaffoldB;
import xyz.edge.ac.checks.checks.scaffold.ScaffoldA;
import xyz.edge.ac.checks.checks.timer.TimerB;
import xyz.edge.ac.checks.checks.timer.TimerA;
import xyz.edge.ac.checks.checks.speed.SpeedE;
import xyz.edge.ac.checks.checks.speed.SpeedD;
import xyz.edge.ac.checks.checks.speed.SpeedC;
import xyz.edge.ac.checks.checks.speed.SpeedB;
import xyz.edge.ac.checks.checks.speed.SpeedA;
import xyz.edge.ac.checks.checks.flight.FlightC;
import xyz.edge.ac.checks.checks.flight.FlightB;
import xyz.edge.ac.checks.checks.flight.FlightA;
import xyz.edge.ac.checks.checks.killaura.KillAuraDX;
import xyz.edge.ac.checks.checks.killaura.KillAuraD3;
import xyz.edge.ac.checks.checks.killaura.KillAuraD;
import xyz.edge.ac.checks.checks.killaura.KillAuraC;
import xyz.edge.ac.checks.checks.killaura.KillAuraBZ;
import xyz.edge.ac.checks.checks.killaura.KillAuraB;
import xyz.edge.ac.checks.checks.killaura.KillAuraA;
import xyz.edge.ac.checks.checks.invalid.InvalidG;
import xyz.edge.ac.checks.checks.invalid.InvalidF;
import xyz.edge.ac.checks.checks.invalid.InvalidE;
import xyz.edge.ac.checks.checks.invalid.InvalidD;
import xyz.edge.ac.checks.checks.invalid.InvalidC;
import xyz.edge.ac.checks.checks.invalid.InvalidB;
import xyz.edge.ac.checks.checks.invalid.InvalidA;
import xyz.edge.ac.checks.checks.clickanalysis.ClickAnalysisE;
import xyz.edge.ac.checks.checks.clickanalysis.ClickAnalysisD;
import xyz.edge.ac.checks.checks.clickanalysis.ClickAnalysisC;
import xyz.edge.ac.checks.checks.clickanalysis.ClickAnalysisB;
import xyz.edge.ac.checks.checks.clickanalysis.ClickAnalysisA;
import xyz.edge.ac.checks.checks.badpacket.BadPacketO;
import xyz.edge.ac.checks.checks.badpacket.BadPacketN;
import xyz.edge.ac.checks.checks.badpacket.BadPacketM;
import xyz.edge.ac.checks.checks.badpacket.BadPacketL;
import xyz.edge.ac.checks.checks.badpacket.BadPacketK;
import xyz.edge.ac.checks.checks.badpacket.BadPacketJ;
import xyz.edge.ac.checks.checks.badpacket.BadPacketI;
import xyz.edge.ac.checks.checks.badpacket.BadPacketH;
import xyz.edge.ac.checks.checks.badpacket.BadPacketG;
import xyz.edge.ac.checks.checks.badpacket.BadPacketF;
import xyz.edge.ac.checks.checks.badpacket.BadPacketE;
import xyz.edge.ac.checks.checks.badpacket.BadPacketD;
import xyz.edge.ac.checks.checks.badpacket.BadPacketC;
import xyz.edge.ac.checks.checks.badpacket.BadPacketB;
import xyz.edge.ac.checks.checks.badpacket.BadPacketA;
import xyz.edge.ac.checks.checks.badpacket.payload.BadPacketD1;
import xyz.edge.ac.checks.checks.badpacket.payload.BadPacketC1;
import xyz.edge.ac.checks.checks.badpacket.payload.BadPacketB1;
import xyz.edge.ac.checks.checks.badpacket.payload.BadPacketA1;
import xyz.edge.ac.checks.checks.attack.AttackB;
import xyz.edge.ac.checks.checks.attack.AttackA;
import xyz.edge.ac.checks.checks.aimbot.AimBotJ;
import xyz.edge.ac.checks.checks.aimbot.AimBotI;
import xyz.edge.ac.checks.checks.aimbot.AimBotH;
import xyz.edge.ac.checks.checks.aimbot.AimBotG;
import xyz.edge.ac.checks.checks.aimbot.AimBotF;
import xyz.edge.ac.checks.checks.aimbot.AimBotE;
import xyz.edge.ac.checks.checks.aimbot.AimBotD;
import xyz.edge.ac.checks.checks.aimbot.AimBotC;
import xyz.edge.ac.checks.checks.aimbot.AimBotB;
import xyz.edge.ac.checks.checks.aimbot.AimBotA;
import xyz.edge.ac.checks.checks.velocity.VelocityB;
import xyz.edge.ac.checks.checks.velocity.VelocityA;
import java.util.HashMap;
import org.bukkit.command.ConsoleCommandSender;
import xyz.edge.ac.auth.Auth;
import org.bukkit.ChatColor;
import xyz.edge.ac.util.color.ColorUtil;
import xyz.edge.ac.config.Config;
import xyz.edge.api.check.DetectionData;
import org.bukkit.Bukkit;
import java.util.Iterator;
import java.util.ArrayList;
import xyz.edge.ac.checks.EdgeCheck;
import xyz.edge.ac.user.User;
import java.lang.reflect.Constructor;
import java.util.List;
import xyz.edge.api.license.LicenseType;
import java.util.Map;

public final class CheckLoader
{
    public static final Map<LicenseType, Class<?>[]> CHECKS;
    private static final List<Constructor<?>> CONSTRUCTORS;
    
    public static List<EdgeCheck> loadChecks(final User user) {
        final List<EdgeCheck> checkList = new ArrayList<EdgeCheck>();
        for (final Constructor<?> constructor : CheckLoader.CONSTRUCTORS) {
            try {
                checkList.add((EdgeCheck)constructor.newInstance(user));
            }
            catch (final Exception exception) {
                System.err.println("Load: " + user.getPlayer().getName());
                exception.printStackTrace();
            }
        }
        return checkList;
    }
    
    public static void setup() {
        Auth.requestLicenseType(response -> {
            if (response == null) {
                Bukkit.getLogger().severe("Failed to load checks.");
            }
            else {
                int loadedEnterpriseChecks = 0;
                int enterpriseFailedChecks = 0;
                for(Map.Entry<LicenseType, Class<?>[]> entry : CheckLoader.CHECKS.entrySet()) {
                    final LicenseType licenseType = entry.getKey();
                    final Class[] array;
                    final Class[] checks = array = entry.getValue();
                    int i = 0;
                    for (int length = array.length; i < length; ++i) {
                        final Class clazz = array[i];
                        final DetectionData detectionData = (DetectionData)clazz.getDeclaredAnnotation(DetectionData.class);
                        if (response.getPriority() < detectionData.licenseType().getPriority()) {
                            ++enterpriseFailedChecks;
                        }
                        else {
                            if (detectionData.licenseType() == LicenseType.ENTERPRISE) {
                                ++loadedEnterpriseChecks;
                            }
                            try {
                                CheckLoader.CONSTRUCTORS.add(clazz.getConstructor(User.class));
                            }
                            catch (final NoSuchMethodException exception) {
                                exception.printStackTrace();
                            }
                            String s;
                            if (Config.ENABLEDCHECKS.contains(clazz.getSimpleName())) {
                                s = ColorUtil.translate("&b&l+ &7" + clazz.getSimpleName() + " &b(" + detectionData.licenseType().name().substring(0, 1).toUpperCase() + detectionData.licenseType().name().substring(1).toLowerCase() + ")");
                            }
                            else {
                                s = ColorUtil.translate("&c&l- &7" + clazz.getSimpleName() + " &b(" + detectionData.licenseType().name().substring(0, 1).toUpperCase() + detectionData.licenseType().name().substring(1).toLowerCase() + ")");
                            }
                            Bukkit.getConsoleSender().sendMessage(s);
                        }
                    }
                }
                if (response == LicenseType.DEVELOPMENT) {
                    Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.RED + "Successfully loaded a total of " + ChatColor.AQUA + CheckLoader.CHECKS.size() + ChatColor.RED + " check" + ((CheckLoader.CHECKS.size() == 1) ? "" : "s") + "!"));
                    //Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.RED + "Thank you for being a developer of Edge!"));
                    Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.RED + "Cracked by 5170."));
                }
                else if (enterpriseFailedChecks > 0) {
                    Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.RED + "Failed to load " + ChatColor.AQUA + enterpriseFailedChecks + ChatColor.RED + " Enterprise check" + ((enterpriseFailedChecks == 1) ? "" : "s") + "!"));
                    //Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.RED + "Upgrade to Enterprise to unlock full benefits of Edge."));
                    Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.RED + "Cracked by 5170."));
                }
                else if (loadedEnterpriseChecks > 0) {
                    Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.GREEN + "Successfully loaded " + ChatColor.AQUA + loadedEnterpriseChecks + ChatColor.GREEN + " Enterprise check" + ((loadedEnterpriseChecks == 1) ? "" : "s") + "!"));
                    //Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.GREEN + "Thanks for being an Edge Enterprise customer!"));
                    Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(ChatColor.RED + "Cracked by 5170."));
                }
            }
        });
    }
    
    static {
        (CHECKS = new HashMap<LicenseType, Class<?>[]>()).put(LicenseType.PREMIUM, new Class[] { VelocityA.class, VelocityB.class, AimBotA.class, AimBotB.class, AimBotC.class, AimBotD.class, AimBotE.class, AimBotF.class, AimBotG.class, AimBotH.class, AimBotI.class, AimBotJ.class, AttackA.class, AttackB.class, BadPacketA1.class, BadPacketB1.class, BadPacketC1.class, BadPacketD1.class, BadPacketA.class, BadPacketB.class, BadPacketC.class, BadPacketD.class, BadPacketE.class, BadPacketF.class, BadPacketG.class, BadPacketH.class, BadPacketI.class, BadPacketJ.class, BadPacketK.class, BadPacketL.class, BadPacketM.class, BadPacketN.class, BadPacketO.class, ClickAnalysisA.class, ClickAnalysisB.class, ClickAnalysisC.class, ClickAnalysisD.class, ClickAnalysisE.class, InvalidA.class, InvalidB.class, InvalidC.class, InvalidD.class, InvalidE.class, InvalidF.class, InvalidG.class, KillAuraA.class, KillAuraB.class, KillAuraBZ.class, KillAuraC.class, KillAuraD.class, KillAuraD3.class, KillAuraDX.class, FlightA.class, FlightB.class, FlightC.class, SpeedA.class, SpeedB.class, SpeedC.class, SpeedD.class, SpeedE.class, TimerA.class, TimerB.class, ScaffoldA.class, ScaffoldB.class, ScaffoldC.class, ScaffoldD.class, ScaffoldE.class, ScaffoldJ.class, ScaffoldF.class, Scaffold2.class, ScaffoldX.class, JesusA.class });
        CONSTRUCTORS = new ArrayList<Constructor<?>>();
    }
}
