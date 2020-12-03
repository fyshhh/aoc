function generate()::Array{Int64, 2}
    input = read("y2020/day3/input.in")
    lines = length(readlines("y2020/day3/input.in"))
    output = filter(x -> x != "\n", (x -> String([x])).(input))
    output = permutedims(reshape(output, :, lines))
    output = map(output) do c
        if (c == ".")
            return 0
        else
            return 1
        end
    end
    return output
end

function count_trees(x::Int64=1, y::Int64=3; arr=generate())::Int64
    count = 0
    xc = 1
    yc = 1
    while xc <= size(arr, 1)
        count += arr[xc, yc]
        xc += x
        yc = ((yc - 1) + y) % size(arr, 2) + 1
    end
    return count
end

function main()
    println(count_trees())
    println(count_trees(1, 1) * count_trees(1, 3) * count_trees(1, 5)
        * count_trees(1, 7) * count_trees(2, 1))
    return nothing
end

main()
