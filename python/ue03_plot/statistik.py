import argparse
import logging
import sys
from datetime import datetime
from logging.handlers import RotatingFileHandler
from subprocess import Popen, PIPE
from dateutil import parser

import matplotlib.pyplot as plt

logger = logging.getLogger("statistik_logger")


def style_plot(commits_num: int):
    # Set labels and title
    # plt.xlabel('X-axis')
    # plt.ylabel('Y-axis')
    plt.title(f'Tobias Hernandez Perez: {commits_num} commits')

    # Spacing
    # plt.xlim(min(x) * 1.1, max(x) * 1.1)
    # plt.ylim(min(y_sin) * 1.1, max(y_sin) * 1.1)

    # Markierungen
    plt.xticks([0, 5, 10, 15, 20, 25])
    plt.yticks([0, 1, 2, 3, 4, 5, 6, 7])

    # LaTeX Ticks
    # plt.xticks([-PI, -PI / 2, -PI / 4, 0, PI / 2, PI],
    #            [r'$-\pi$', r'$-\pi/2$', r'$-\pi/4$', r'$0$', r'$+\pi/2$', r'$+\pi$'])
    plt.yticks([0, 1, 2, 3, 4, 5, 6],
               ["mon", "tue", "wed", "thu", "fri", "sat", "sun"])

    # Legende
    # plt.plot(x, y_cos, color="yellow", linewidth=2.5, linestyle="-", label="cosine")
    # plt.plot(x, y_sin, color="cyan", linewidth=2.5, linestyle="-", label="sine")
    # plt.legend(loc='upper left', frameon=False)

    # Achsen
    ax = plt.gca()
    # ax.spines['right'].set_color('none')
    # ax.spines['top'].set_color('none')
    # ax.xaxis.set_ticks_position('bottom')
    # ax.spines['bottom'].set_position(('data', 0))
    # ax.yaxis.set_ticks_position('left')
    # ax.spines['left'].set_position(('data', 0))

    # Besonders Kennzeichnen
    # t = 2 * PI / 3
    # plt.plot([t, t], [0, math.cos(t)], color='blue', linewidth=2.5, linestyle="--")
    # plt.scatter([t, ], [math.cos(t), ], 50, color='blue')
    # plt.annotate(r'$\sin(\frac{2\pi}{3})=\frac{\sqrt{3}}{2}$',
    #              xy=(t, math.sin(t)), xycoords='data',
    #              xytext=(+10, +30), textcoords='offset points', fontsize=16,
    #              arrowprops=dict(arrowstyle="->", connectionstyle="arc3,rad=.2"))
    # plt.plot([t, t], [0, math.sin(t)], color='red', linewidth=2.5, linestyle="--")
    # plt.scatter([t, ], [math.sin(t), ], 50, color='red')
    # plt.annotate(r'$\cos(\frac{2\pi}{3})=-\frac{1}{2}$',
    #              xy=(t, math.cos(t)), xycoords='data',
    #              xytext=(-90, -50), textcoords='offset points', fontsize=16,
    #              arrowprops=dict(arrowstyle="->", connectionstyle="arc3,rad=.2"))

    # Bessere Sichtbarkeit der Ticks
    for label in ax.get_xticklabels() + ax.get_yticklabels():
        label.set_fontsize(16)
        label.set_bbox(dict(facecolor='white', edgecolor='None', alpha=0.65))

    ax.set_axisbelow(True)


def create_git_scatter():
    git_log = ["git", "log", "--pretty=format:%h|%an|%ad|%s", "--date=format-local:%Y-%m-%d %H:%M:%S"]
    process = Popen(git_log, stdout=PIPE, stderr=PIPE, text=True)
    out, err = process.communicate()

    log_entries = out.splitlines()

    commits_by_weekday_hour = {}
    commits_num = 0

    for entry in log_entries:
        entry_components = entry.split('|')

        commit_hash, author_name, commit_date_time, commit_message = entry_components

        parsed_date = parser.parse(commit_date_time)
        weekday = parsed_date.weekday()
        hour = parsed_date.hour

        key = (weekday, hour)
        commits_by_weekday_hour[key] = commits_by_weekday_hour.get(key, 0) + 10

        commits_num += 1

    weekdays, hours = zip(*commits_by_weekday_hour.keys())
    counts = list(commits_by_weekday_hour.values())

    plt.scatter(hours, weekdays, s=counts, c='blue', alpha=0.5)
    style_plot(commits_num)
    plt.show()


def create_logger():
    """
    creates the variables necessary for logging
    :return:
    """
    rot_file_handler = RotatingFileHandler('create_user.log', maxBytes=10_000, backupCount=5)
    stream_handler = logging.StreamHandler(sys.stdout)
    logger.addHandler(rot_file_handler)
    logger.addHandler(stream_handler)


def parse_args():
    """
    The necessary commands to make the command line tools usable
    :return:
    """
    create_logger()

    argparser = argparse.ArgumentParser()
    argparser.add_argument("folder")
    argparser.add_argument('-v', "--verbosity", help="increase output verbosity", action="store_true")
    argparser.add_argument("-q", "--quiet", help="decrease output verbosity", action="store_true")

    args = argparser.parse_args()

    if args.verbosity:
        print("verbosity turned on")
        logger.setLevel(logging.DEBUG)
    elif args.quiet:
        logger.setLevel(logging.NOTSET)

    if args.folder:
        pass


if __name__ == '__main__':
    create_git_scatter()
    # print(out)
    # parse_args()
