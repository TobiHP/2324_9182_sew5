if __name__ == '__main__':
    red = 12
    green = 13
    blue = 14

    sum = 0

    with open("inputs/day2") as f:
        for line in f.readlines():
            split = line.strip().split(": ")
            game_id = int(split[0].split()[1])
            sets = split[1].split("; ")
            is_possible = True
            for cubes in sets:
                colors = cubes.split(", ")
                for color in colors:
                    color_num = int(color.split(" ")[0])
                    color_name = color.split(" ")[1]
                    match color_name:
                        case "red":
                            if color_num > red:
                                is_possible = False
                        case "green":
                            if color_num > green:
                                is_possible = False
                        case "blue":
                            if color_num > blue:
                                is_possible = False

            if is_possible:
                sum += game_id

        print(sum)

        # possible 55099
