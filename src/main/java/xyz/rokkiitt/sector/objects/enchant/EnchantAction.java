package xyz.rokkiitt.sector.objects.enchant;

import cn.nukkit.item.enchantment.*;
import cn.nukkit.*;
import cn.nukkit.item.*;
import xyz.rokkiitt.sector.Settings;
import xyz.rokkiitt.sector.utils.Util;
import cn.nukkit.command.*;

public class EnchantAction
{
    private Enchantment enchant;
    private int levelCost;
    
    public EnchantAction(final Enchantment e, final int levelCost) {
        this.enchant = e;
        this.levelCost = levelCost;
    }
    
    public Item execute(final Player player, final Item item) {
        if (item != null) {
            if (player.getExperienceLevel() >= this.levelCost || player.getGamemode() == 1) {
                if (this.enchant.canEnchant(item)) {
                    if (item.hasEnchantment(this.enchant.getId())) {
                        if (item.getEnchantment(this.enchant.getId()).getLevel() >= this.enchant.getLevel()) {
                            Util.sendMessage((CommandSender)player, Settings.getMessage("enchantalreadyhas"));
                        }
                        else {
                            item.addEnchantment(new Enchantment[] { this.enchant });
                            if (player.getGamemode() != 1) {
                                player.setExperience(player.getExperience(), player.getExperienceLevel() - this.levelCost);
                            }
                        }
                    }
                    else {
                        item.addEnchantment(new Enchantment[] { this.enchant });
                        if (player.getGamemode() != 1) {
                            player.setExperience(player.getExperience(), player.getExperienceLevel() - this.levelCost);
                        }
                    }
                }
                else {
                    Util.sendMessage((CommandSender)player, Settings.getMessage("enchantcantadd"));
                }
            }
            else {
                Util.sendMessage((CommandSender)player, Settings.getMessage("enchantnoxp").replace("{EXP}", String.valueOf(this.levelCost)));
            }
        }
        else {
            Util.sendMessage((CommandSender)player, Settings.getMessage("enchantcant"));
        }
        return item;
    }
}
