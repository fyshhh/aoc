function generate()::Array{Int64}
    array = Array{Int64}(undef, 0)
    io = open("y2020/day1/input.in")
    for line in eachline(io)
        append!(array, parse(Int64, line))
    end
    close(io)
    return array
end

function partone(arr::Array{Int64})
    for i in arr, j in arr
        if i + j == 2020
            println(i * j)
            break
        end
    end
    return nothing
end

function parttwo(arr::Array{Int64})
    for i in arr, j in arr, k in arr
        if i + j + k == 2020
            println(i * j * k)
            break
        end
    end
    return nothing
end

function main()
    array = generate()
    partone(array)
    parttwo(array)
end

main()
