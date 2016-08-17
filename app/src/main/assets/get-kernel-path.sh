#!/system/bin/sh
kernel_list=(
/dev/block/platform/mtk-msdc.0/11230000.msdc0/by-name/boot
/dev/block/platform/omap/omap_hsmmc.0/by-name/boot
/dev/block/platform/omap/omap_hsmmc.1/by-name/boot
/dev/block/platform/sdhci-pxav3.2/by-name/KERNEL
/dev/block/platform/sprd-sdhci.3/by-name/KERNEL
/dev/block/platform/sdhci-tegra.3/by-name/LNX
/dev/block/platform/15570000.ufs/by-name/BOOT
/dev/block/platform/155a0000.ufs/by-name/BOOT
/dev/block/platform/msm_sdcc.1/by-name/Kernel
/dev/block/platform/mtk-msdc.0/by-name/boot
/dev/block/platform/msm_sdcc.1/by-name/boot
/dev/block/platform/sdhci.1/by-name/KERNEL
/dev/block/platform/hi_mci.0/by-name/boot
/dev/block/platform/sdhci.1/by-name/boot
/dev/block/bootdevice/by-name/boot
/dev/block/platform/emmc/by-name/boot
/dev/block/by-name/boot
/dev/block/nandc
/dev/bootimg
/dev/boot
/dev/block/platform/dw_mmc.0/by-name/BOOT
)
for path in ${kernel_list[@]};do
	if [[ -e "$path" ]];then
		echo "KERNEL_PATH=$path"
	fi
done
