import tkinter as tk

def draw_line(canvas, x0, y0, x1, y1):
    canvas.create_line(x0, y0, x1, y1, width=2, fill="white")

def draw_coords(canvas, coords):
    for i in range(len(coords)):
        draw_line(canvas, coords[i][0], coords[i][1], coords[(i+1)%len(coords)][0], coords[(i+1)%len(coords)][1])

def auto_resize_canvas(canvas, coords):
    min_x = min(coord[0] for coord in coords)
    min_y = min(coord[1] for coord in coords)
    max_x = max(coord[0] for coord in coords)
    max_y = max(coord[1] for coord in coords)

    canvas.config(width=max_x - min_x, height=max_y - min_y + 20)

with open("coords.txt") as f:
    coords = []
    multiplier = 7

    root = tk.Tk()
    root.title("Visualizer")

    while (line := f.readline()):
        coords.append((int(line.split(" ")[0]) * multiplier, int(line.split(" ")[1])*multiplier))

    canvas = tk.Canvas(root, width=1, height=1, background="black")
    canvas.pack()

    draw_coords(canvas, coords)
    auto_resize_canvas(canvas, coords)

    root.mainloop()
