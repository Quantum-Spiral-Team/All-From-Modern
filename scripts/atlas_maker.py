import os
import re
from PIL import Image

PROJECT_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS_ROOT = os.path.join(PROJECT_ROOT, '.assets')
OUTPUT_ROOT = os.path.join(PROJECT_ROOT, 'src', 'main', 'resources', 'assets')

class AtlasConfig:
    def __init__(self, name, source_subpath, output_subpath, tile_size=18, mode='compact'):
        self.name = name
        self.source_dir = os.path.join(ASSETS_ROOT, source_subpath)
        self.output_path = os.path.join(OUTPUT_ROOT, output_subpath)
        self.tile_size = tile_size
        self.mode = mode # 'grid' or 'compact'

def create_atlas(config: AtlasConfig):
    if not os.path.exists(config.source_dir):
        print(f"Skipping {config.name}: Source dir not found.")
        return

    files = [f for f in os.listdir(config.source_dir) if f.endswith('.png')]
    if not files: return

    if config.mode == 'grid':
        generate_grid_atlas(config, files)
    else:
        generate_compact_atlas(config, files)

def generate_grid_atlas(config: AtlasConfig, files):
    pattern = re.compile(r'^(\d+)-(\d+)-(.+)\.png$')
    to_process = []
    max_gx = max_gy = 0

    for filename in files:
        if match := pattern.match(filename):
            gx, gy = int(match.group(1)), int(match.group(2))
            to_process.append((gx, gy, filename))
            max_gx, max_gy = max(max_gx, gx), max(max_gy, gy)

    atlas_w = (max_gx + 1) * config.tile_size
    atlas_h = (max_gy + 1) * config.tile_size
    atlas = Image.new('RGBA', (atlas_w, atlas_h), (0, 0, 0, 0))

    for gx, gy, filename in to_process:
        with Image.open(os.path.join(config.source_dir, filename)) as img:
            atlas.paste(img.convert('RGBA'), (gx * config.tile_size, gy * config.tile_size))

    save_atlas(atlas, config.output_path)

def generate_compact_atlas(config, files):
    import math
    n = len(files)
    columns = math.ceil(math.sqrt(n))
    rows = math.ceil(n / columns)

    atlas_w = columns * config.tile_size
    atlas_h = rows * config.tile_size
    atlas = Image.new('RGBA', (atlas_w, atlas_h), (0, 0, 0, 0))

    for index, filename in enumerate(sorted(files)):
        gx = index % columns
        gy = index // columns
        with Image.open(os.path.join(config.source_dir, filename)) as img:
            atlas.paste(img.convert('RGBA'), (gx * config.tile_size, gy * config.tile_size))

    save_atlas(atlas, config.output_path)

def save_atlas(image, path):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    image.save(path)
    print(f"Success: Atlas saved to {path} ({image.width}x{image.height})")

if __name__ == "__main__":
    configs = [
        AtlasConfig(
            "Potions",
            "potions",
            "afm/textures/gui/potions_atlas.png",
            mode="grid"
        )
    ]

    for cfg in configs:
        create_atlas(cfg)
