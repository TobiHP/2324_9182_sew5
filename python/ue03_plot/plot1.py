import numpy as np
import matplotlib.pyplot as plt
import math

PI = math.pi

# Generate x values from 0 to 2*pi
x = np.linspace(0, 2 * np.pi, 1000)

# Generate y values using the sine function
y = np.sin(x)

# Plot the sine wave
plt.plot(x, y)

# Set labels and title
plt.xlabel('X-axis')
plt.ylabel('Y-axis')
plt.title('Sine Wave')

# Show the plot
plt.savefig("plot1_hernandezperez.png", dpi=72)
plt.show()

# TODO sorry aber warum funktioniert KEIN Code in der Angabe?
# -> ist der nur da um Schueler*innen zu verwirren, ernsthaft!??!?!?!
# plt.plot(X, C, color="blue", linewidth=2.5, linestyle="-")
# plt.plot(X, S, color="red", linewidth=2.5, linestyle="-")

