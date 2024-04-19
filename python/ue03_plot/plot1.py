import math

import matplotlib.pyplot as plt
import numpy as np

# Generate x values from -pi to +pi
PI = np.pi
x = np.linspace(-PI, PI, 1000)

# Generate y values using the sine function
y_sin = np.sin(x)
y_cos = np.cos(x)

# Plot the sine wave
# plt.plot(x, y_sin)
# plt.plot(x, y_cos)

# Set labels and title
plt.xlabel('X-axis')
plt.ylabel('Y-axis')
plt.title('Plot von Tobias Hernandez Perez, HTL3R')

# Spacing
plt.xlim(min(x) * 1.1, max(x) * 1.1)
plt.ylim(min(y_sin) * 1.1, max(y_sin) * 1.1)

# Markierungen
plt.xticks([-PI, -PI / 2, 0, PI / 4, PI / 2, PI])
plt.yticks([-1, 0, +1])

# LaTeX Ticks
plt.xticks([-PI, -PI / 2, -PI / 4, 0, PI / 2, PI],
           [r'$-\pi$', r'$-\pi/2$', r'$-\pi/4$', r'$0$', r'$+\pi/2$', r'$+\pi$'])
plt.yticks([-1, 0, +1],
           [r'$-1$', r'$0$', r'$+1$'])

# Legende
plt.plot(x, y_cos, color="purple", linewidth=2.5, linestyle="--", label="cosine")
plt.plot(x, y_sin, color="cyan", linewidth=2.5, linestyle="--", label="sine")
plt.legend(loc='upper left', frameon=False)

# Achsen
ax = plt.gca()
ax.spines['right'].set_color('none')
ax.spines['top'].set_color('none')
ax.xaxis.set_ticks_position('bottom')
ax.spines['bottom'].set_position(('data', 0))
ax.yaxis.set_ticks_position('left')
ax.spines['left'].set_position(('data', 0))

# Besonders Kennzeichnen
t = 2 * PI / 3
plt.plot([t, t], [0, math.cos(t)], color='blue', linewidth=2.5, linestyle="--")
plt.scatter([t, ], [math.cos(t), ], 50, color='blue')
plt.annotate(r'$\sin(\frac{2\pi}{3})=\frac{\sqrt{3}}{2}$',
             xy=(t, math.sin(t)), xycoords='data',
             xytext=(+10, +30), textcoords='offset points', fontsize=16,
             arrowprops=dict(arrowstyle="->", connectionstyle="arc3,rad=.2"))
plt.plot([t, t], [0, math.sin(t)], color='red', linewidth=2.5, linestyle="--")
plt.scatter([t, ], [math.sin(t), ], 50, color='red')
plt.annotate(r'$\cos(\frac{2\pi}{3})=-\frac{1}{2}$',
             xy=(t, math.cos(t)), xycoords='data',
             xytext=(-90, -50), textcoords='offset points', fontsize=16,
             arrowprops=dict(arrowstyle="->", connectionstyle="arc3,rad=.2"))

plt.plot([-PI / 4, -PI / 4], [0, math.cos(-PI / 4)], color='red', linewidth=2.5, linestyle="--")
plt.plot([-PI / 4, -PI / 4], [0, math.sin(-PI / 4)], color='red', linewidth=2.5, linestyle="--")


# Bessere Sichtbarkeit der Ticks
for label in ax.get_xticklabels() + ax.get_yticklabels():
    label.set_fontsize(16)
    label.set_bbox(dict(facecolor='white', edgecolor='None', alpha=0.65))

ax.set_axisbelow(True)

# Pfeile (kinda)
ax.annotate("", xy=(max(x) * 1.1, 0), xytext=(0, 0), arrowprops=dict(arrowstyle="->"))
ax.annotate("", xy=(0, max(y_sin) * 1.1), xytext=(0, 0), arrowprops=dict(arrowstyle="->"))

# Show the plot
plt.savefig("plot1_hernandezperez.png", dpi=72)
plt.show()
