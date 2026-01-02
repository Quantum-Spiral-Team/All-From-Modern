import os
import re
from PIL import Image

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
SOURCE_DIR = os.path.join(SCRIPT_DIR, '.assets', 'potions')
OUTPUT_PATH = os.path.join(SCRIPT_DIR, 'src', 'main', 'resources', 'assets', 'afm', 'textures', 'gui', 'potions_atlas.png')
TILE_SIZE = 18

def create_dynamic_atlas():
    pattern = re.compile(r'^(\d+)-(\d+)-(.+)\.png$')
    to_process = []
    max_grid_x = max_grid_y = 0

    for filename in os.listdir(SOURCE_DIR):
        if match := pattern.match(filename):
            gx, gy = int(match.group(1)), int(match.group(2))
            to_process.append((gx, gy, filename))
            max_grid_x = max(max_grid_x, gx)
            max_grid_y = max(max_grid_y, gy)

    atlas_w = (max_grid_x + 1) * TILE_SIZE
    atlas_h = (max_grid_y + 1) * TILE_SIZE
    atlas = Image.new('RGBA', (atlas_w, atlas_h), (0, 0, 0, 0))

    for gx, gy, filename in to_process:
        with Image.open(os.path.join(SOURCE_DIR, filename)) as img:
            atlas.paste(img.convert('RGBA'), (gx * TILE_SIZE, gy * TILE_SIZE))

    os.makedirs(os.path.dirname(OUTPUT_PATH), exist_ok=True)
    atlas.save(OUTPUT_PATH)
    print(f"Atlas {atlas_w}x{atlas_h} saved to {OUTPUT_PATH}")

if __name__ == "__main__":
    create_dynamic_atlas()