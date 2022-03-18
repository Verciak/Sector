package xyz.rokkiitt.sector.objects.itemshop;

public class ItemShop
{
    public int id;
    public int type;
    public String owner;
    
    public String getType() {
        switch (this.type) {
            case 0: {
                return "Vip";
            }
            case 1: {
                return "Svip";
            }
            case 2: {
                return "Sponsor";
            }
            case 3: {
                return "Sejf x1";
            }
            case 4: {
                return "Lom x1";
            }
            case 5: {
                return "Turbodrop 1h";
            }
            case 6: {
                return "Pandora x16";
            }
            case 7: {
                return "Pandora x32";
            }
            case 8: {
                return "Pandora x64";
            }
            case 9: {
                return "Pandora x128";
            }
            case 10: {
                return "Pandora x256";
            }
            case 11: {
                return "Pandora x512";
            }
            default: {
                return "";
            }
        }
    }
}
