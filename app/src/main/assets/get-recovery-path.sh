#!/system/bin/sh
recovery_list=(
/dev/block/platform/mtk-msdc.0/11230000.msdc0/by-name/recovery
/dev/block/platform/omap/omap_hsmmc.0/by-name/recovery
/dev/block/platform/omap/omap_hsmmc.1/by-name/recovery
/dev/block/platform/sdhci-tegra.3/by-name/recovery
/dev/block/platform/sdhci-pxav3.2/by-name/RECOVERY
/dev/block/platform/msm_sdcc.1/by-name/FOTAKernel
/dev/block/platform/15570000.ufs/by-name/RECOVERY
/dev/block/platform/155a0000.ufs/by-name/RECOVERY
/dev/block/platform/comip-mmc.1/by-name/recovery
/dev/block/platform/msm_sdcc.1/by-name/recovery
/dev/block/platform/mtk-msdc.0/by-name/recovery
/dev/block/platform/sdhci-tegra.3/by-name/SOS
/dev/block/platform/sdhci-tegra.3/by-name/USP
/dev/block/platform/dw_mmc.0/by-name/recovery
/dev/block/platform/dw_mmc.0/by-name/RECOVERY
/dev/block/platform/hi_mci.1/by-name/recovery
/dev/block/platform/hi_mci.0/by-name/recovery
/dev/block/platform/sdhci-tegra.3/by-name/UP
/dev/block/platform/sdhci-tegra.3/by-name/SS
/dev/block/platform/sdhci.1/by-name/RECOVERY
/dev/block/platform/sdhci.1/by-name/recovery
/dev/block/platform/dw_mmc/by-name/recovery
/dev/block/platform/dw_mmc/by-name/RECOVERY
/dev/block/bootdevice/by-name/recovery
/dev/block/by-name/recovery
/dev/block/recovery
/dev/block/nandg
/dev/block/acta
/dev/recovery
/dev/block/platform/dw_mmc.0/by-name/RECOVERY
)
for path in ${recovery_list[@]};do
	if [[ -e "$path" ]];then
		echo "RECOVERY_PATH=$path"
	fi
done
