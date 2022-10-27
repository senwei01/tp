package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ArgumentMultimap.arePrefixesPresent;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_LOCATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INCOME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MONTHLY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PLANTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RISKTAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FindPredicate;
import seedu.address.model.person.IncomeContainsKeywordsPredicate;
import seedu.address.model.person.IncomeLevel;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.NormalTagContainsKeywordsPredicate;
import seedu.address.model.person.Phone;
import seedu.address.model.person.PhoneContainsKeywordsPredicate;
import seedu.address.model.person.PlanTagContainsKeywordsPredicate;
import seedu.address.model.person.RiskTagContainsKeywordsPredicate;
import seedu.address.model.tag.NormalTag;
import seedu.address.model.tag.PlanTag;
import seedu.address.model.tag.RiskTag;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {
    private static final String SPACE_REGEX = "\\s+";
    private static final String PLAN_REGEX = "(?<!\\G\\S+)\\s";

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }
        List<FindPredicate> predicates = new ArrayList<>();
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_RISKTAG,
                        PREFIX_PLANTAG, PREFIX_TAG, PREFIX_INCOME);

        if (arePrefixesPresent(argMultimap, PREFIX_ADDRESS, PREFIX_INCOME, PREFIX_MONTHLY, PREFIX_EMAIL,
                PREFIX_APPOINTMENT_DATE, PREFIX_APPOINTMENT_LOCATION)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            List<Name> names = ParserUtil.parseAllSpaceSeparatedNames(argMultimap
                    .getAllValuesSeparatedByRegex(PREFIX_NAME, SPACE_REGEX));
            predicates.add(new NameContainsKeywordsPredicate(names.stream()
                    .map(x -> x.toString()).collect(Collectors.toList())));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            List<Phone> phones = ParserUtil.parseAllSpaceSeparatedPhone(argMultimap
                    .getAllValuesSeparatedByRegex(PREFIX_PHONE, SPACE_REGEX));
            predicates.add(new PhoneContainsKeywordsPredicate(phones.stream()
                    .map(x -> x.toString()).collect(Collectors.toList())));
        }
        if (argMultimap.getValue(PREFIX_RISKTAG).isPresent()) {
            List<RiskTag> riskTags = ParserUtil.parseAllSpaceSeparatedRiskTag(argMultimap
                    .getAllValuesSeparatedByRegex(PREFIX_RISKTAG, SPACE_REGEX));
            predicates.add(new RiskTagContainsKeywordsPredicate(riskTags.stream()
                    .map(x -> x.tagName).collect(Collectors.toList())));
        }
        if (argMultimap.getValue(PREFIX_PLANTAG).isPresent()) {
            List<PlanTag> planTags = ParserUtil.parseAllSpaceSeparatedPlanTags(argMultimap
                    .getAllValuesSeparatedByRegex(PREFIX_PLANTAG, PLAN_REGEX));
            predicates.add(new PlanTagContainsKeywordsPredicate(planTags.stream()
                    .map(x -> x.tagName).collect(Collectors.toList())));
        }
        if (argMultimap.getValue(PREFIX_TAG).isPresent()) {
            List<NormalTag> normalTags = ParserUtil.parseAllSpaceSeparatedNormalTags(argMultimap
                    .getAllValuesSeparatedByRegex(PREFIX_TAG, SPACE_REGEX));
            predicates.add(new NormalTagContainsKeywordsPredicate(normalTags.stream()
                    .map(x -> x.tagName).collect(Collectors.toList())));
        }
        if (argMultimap.getValue(PREFIX_INCOME).isPresent()) {
            List<IncomeLevel> incomeLevels = ParserUtil.parseAllSpaceSeparatedIncomeLevels(argMultimap
                    .getAllValuesSeparatedByRegex(PREFIX_INCOME, SPACE_REGEX));
            predicates.add(new IncomeContainsKeywordsPredicate(incomeLevels.stream().map(x -> x.value)
                    .collect(Collectors.toList()), ">"));
        }

        return new FindCommand(predicates);
    }

    private void checkIfRiskTag(String[] tags) throws ParseException {
        for (String tag : tags) {
            // if tag is not a valid Risk tag
            if (!RiskTag.isRiskTag(tag)) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, RiskTag.MESSAGE_CONSTRAINTS));
            }
        }
    }

}
