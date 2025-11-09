import pandas as pd
from matplotlib import pyplot as plt


file_path = "csv/contracted-1000.csv"
data = pd.read_csv(file_path)

print(data)

plotsData = [
    [
        "Dijkstra Time", "Bidirectional Dijkstra Time", "Contraction Hierarchy Time",
    ], 
    [
        "Dijkstra Relaxed", "Bidirectional Dijkstra Relaxed", "Contraction Hierarchy Relaxed",
    ]
]

def createPlot(data, columns, unit, title, dataMaper = lambda x: x):
    x = []
    means = []
    maxs = []
    mins = []
    print(columns)
    for column in columns:
        x.append(data[column])
        means.append(data[column].mean())
        maxs.append(data[column].max())
        mins.append(data[column].min())
    _, ax = plt.subplots()
    for i, mean in enumerate(means, start=1):
        y = maxs[i-1] + 0.05 * (maxs[i-1] - mins[i-1])  # small gap above box
        plt.text(i, y, f"{dataMaper(mean):.2f} {unit}", ha="center", va="bottom", fontsize=9, color="blue")
    ax.boxplot(x, tick_labels=columns, showmeans=True)
    ax.set_yscale("log")
    plt.xticks(rotation=15, fontsize=10)
    plt.title(title)
    plt.tight_layout()
    plt.savefig(f"{title}.png")
    plt.show()
    
createPlot(data, plotsData[0], "ms", "Running Time", lambda x: x / 1e6)
createPlot(data, plotsData[1], "kE", "Relaxed Edges", lambda x: x / 1e3)