function isvalidone(input::AbstractString)::Bool
    return occursin("byr", input) &&
        occursin("iyr", input) &&
        occursin("eyr", input) &&
        occursin("hgt", input) &&
        occursin("hcl", input) &&
        occursin("ecl", input) &&
        occursin("pid", input)
end

function isvalidpassport(byr, ecl, eyr, hcl, hgt, iyr, pid)
    if byr < "1920" || byr > "2002"
        return false
    end
    if iyr < "2010" || iyr > "2020"
        return false
    end
    if eyr < "2020" || eyr > "2030"
        return false
    end
    if endswith(hgt, "cm")
        if hgt < "150" || hgt > "194"
            return false
        end
    elseif endswith(hgt, "in")
        if hgt < "59" || hgt > "77"
            return false
        end
    else
        return false
    end
    if !startswith(hcl, "#") ||
        length(collect(eachmatch(r"[0-9]|[a-f]", hcl))) != 6
        return false
    end
    if !occursin(r"(amb)|(blu)|(brn)|(gry)|(grn)|(hzl)|(oth)", ecl)
        return false
    end
    if length(pid) != 9 || pid < "000000000" || pid > "999999999"
        return false
    end
    return true
end

function isvalidtwo(input::AbstractString)::Bool
    inputs = split(input)
    filter!(str -> !startswith(str, "cid"), inputs)
    sort!(inputs)
    map!(str -> split(str, ":")[2], inputs, inputs)
    return isvalidpassport(inputs...)
end

function main()
    inputs = split(read("y2020/day4/input.in", String), "\n\n")
    map!(str -> replace(str, "\n" => " "), inputs, inputs)
    filter!(isvalidone, inputs)
    println(length(inputs))
    filter!(isvalidtwo, inputs)
    println(length(inputs))
end

main()
